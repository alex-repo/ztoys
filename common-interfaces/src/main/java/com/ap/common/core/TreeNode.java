/**
 * Copyright 2015 alex
 * <p>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ap.common.core;

import com.ap.common.util.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface TreeNode<T extends TreeNode> extends Listened {

    TreeNode getParent();

    void setParent(TreeNode tree);

    ArrayList<T> getChildrenList();

    Stream<T> getChildrenStream();

    default void alarm() {
        if (getParent() != null)
            getParent().fireEvent(new EventObject(this));
    }

    default T treeClone() {
        try {
            byte[] buffer = IOUtils.ObjectToBytes(this);
            return (T) IOUtils.bytesToObject(buffer);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    default <E extends TreeNode> Stream<TreeNode> flattened(final Class<E> clazz) {
        return Stream.concat(Stream.of(this).parallel()
                        .filter(tree -> tree.getClass().equals(clazz))
                , getChildrenStream().parallel().flatMap(tree -> tree.flattened(clazz)));
    }

    Predicate<TreeNode> predicate = (tree) -> tree != null;

    default Stream<TreeNode> flattened() {
        return Stream.concat(Stream.of(this).filter(predicate), getChildrenStream().flatMap(TreeNode::flattened));
    }

}
