package com.ap.common.util;

import com.ap.common.image.AbstractImageWrapper;
import com.ap.common.image.IImage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Pattern;

public class IOUtils {

    public static void save(String filePath, Serializable object) {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(filePath))))) {
            out.writeObject(object);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object createFromFile(String filePath) throws ClassNotFoundException, IOException {
        File file = new File(filePath);
        return createFromFile(file);
    }

    public static Object createFromFile(File file) throws IOException, ClassNotFoundException {

        try (ObjectInputStream oistream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            return oistream.readObject();
        }
    }

    public static void listFilesForFolderAsStream(final File folder, final Collection<InputStream> streams, final CharSequence charSequence)
            throws FileNotFoundException {

        for (final File fileEntry : Objects.requireNonNull(folder).listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolderAsStream(fileEntry, streams, charSequence);
            } else {
                if (fileEntry.getName().contains(charSequence)) {
                    streams.add(new FileInputStream(fileEntry));
                }
            }
        }
    }

    public static void listFilesForFolderAsFile(final Collection<File> files, final CharSequence charSequence) throws FileNotFoundException {
        listFilesForFolderAsFile(new File("."), files, charSequence);
    }

    public static void listFilesForFolderAsFile(final File folder, final Collection<File> files, final CharSequence charSequence) throws FileNotFoundException {
        for (final File fileEntry : Objects.requireNonNull(folder).listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolderAsFile(fileEntry, files, charSequence);
            } else {
                if (fileEntry.getName().contains(charSequence)) {
                    files.add(fileEntry);
                }
            }
        }
    }

    public static String findProperty(String name) {
        final File folder = new File(".");
        List<InputStream> streams = new ArrayList<>();
        try {
            listFilesForFolderAsStream(folder, streams, ".properties");
            if (streams.size() > 0) {
                return fetchProperty(streams.get(0), name);
            }

            Pattern pattern = Pattern.compile(".*.properties");
            final String classPath = System.getProperty("java.class.path", ".");
            final Collection<String> list = ResourceList.getResources(pattern, classPath);
            Iterator<String> listIterator = list.iterator();
            if (listIterator.hasNext()) {
                InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(listIterator.next());
                return fetchProperty(inputStream, name);
            }

        } catch (IOException ioe) {
            CommonUtils.getLogger(IOUtils.class).debug(ioe);
        }
        return null;
    }

    private static String fetchProperty(InputStream inputStream, String name) {
        Properties props = new Properties();
        try {
            props.load(inputStream);
            if (props.containsKey(name)) {
                return props.getProperty(name);
            }
        } catch (Exception ioe) {
            CommonUtils.getLogger(IOUtils.class).debug(ioe);
        }
        return null;
    }

    public static <T> Object bytesToObject(byte[] buffer) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return objectInputStream.readObject();
        }

    }

    public static byte[] ObjectToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    public static File[] getImageFiles(String filesPath) {

        File fpath = new File(filesPath);

        File[] files = null;
        if (fpath.isDirectory()) {
            files = fpath.listFiles();
        } else if (isImage(fpath)) {
            files = new File[]{fpath};
        }
        if (files == null) {
            files = new File[]{};
        }

        CommonUtils.getLogger(IOUtils.class).debug("Working User Directory = " + System.getProperty("user.dir"));
        CommonUtils.getLogger(IOUtils.class).debug("Load files from Directory = " + fpath.getPath());
        files = IOUtils.filterImage(files);
        CommonUtils.getLogger(IOUtils.class).debug("Selected files = " + files.length);

        return files;
    }

    public static File[] filterImage(File[] files) {
        return Arrays.stream(files).filter(f -> {
            if (isImage(f)) {
                CommonUtils.getLogger(IOUtils.class).trace("File = " + f.getPath() + " selected");
                return true;
            } else {
                CommonUtils.getLogger(IOUtils.class).trace("File = " + f.getPath() + " skipped");
                return false;
            }
        }).toArray(File[]::new);
    }

    private static boolean isImage(File f) {
        String n = f.getPath();
        return n.contains(".png") || n.contains(".gif") || n.contains(".jpeg") || n.contains(".png");
    }

    public static List<IImage> createImages(File[] files) {
        List<IImage> images = new ArrayList<>();
        for (File file : files) {
            try {
                images.add(new AbstractImageWrapper(file));
            } catch (Exception ex) {
                CommonUtils.getLogger(IOUtils.class).error(ex);
            }
        }
        return images;
    }

    public static List<IImage> createImages(String filesPath) {
        List<IImage> images = new ArrayList<>();
        File[] files = IOUtils.getImageFiles(filesPath);
        if (files != null) {
            images = IOUtils.createImages(files);
        }
        return images;
    }

}
