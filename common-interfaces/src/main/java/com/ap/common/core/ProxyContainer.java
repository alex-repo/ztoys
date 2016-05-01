package com.ap.common.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyContainer<K, V> implements Serializable {

    transient private Container<V> container;

    public Container<V> getContainer() {
        return container;
    }

    class SerializableInvocationHandler implements InvocationHandler, Serializable {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(object, args);
        }
    }

    private InvocationHandler proxyInvocationHandler = new SerializableInvocationHandler();
    private Container<V> object;

    @SuppressWarnings("unused")
    private ProxyContainer() {
    }

    public ProxyContainer(Class<? extends Container<V>> containerClass) {

        try {
            Class<?> interfaceClass = Class.forName(Container.class.getName());
            container = (Container<V>) Proxy.newProxyInstance(
                    getClass().getClassLoader()
                    , new Class<?>[]{interfaceClass}
                    , proxyInvocationHandler
            );
            Class<?> objectClass = Class.forName(containerClass.getName());
            object = (Container<V>) objectClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        Class<?> interfaceClass = Class.forName(Container.class.getName());
        container = (Container<V>) Proxy.newProxyInstance(
                getClass().getClassLoader()
                , new Class<?>[]{interfaceClass}
                , proxyInvocationHandler
        );
    }
}
