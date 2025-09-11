package org.example.auth.fileIO;

import java.io.*;

public class TextFileRW {
    private static volatile TextFileRW instance;

    private TextFileRW() { }

    public static TextFileRW getInstance() {
        if (instance == null) {
            synchronized (TextFileRW.class) {
                if (instance == null) {
                    instance = new TextFileRW();
                }
            }
        }
        return instance;
    }

    public void write(final String input, final String path, boolean append) throws IOException {
        final File file = new File(path);
        final File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (FileWriter fw = new FileWriter(file, append);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(input);
        }
    }

    public String read(final String path) throws IOException {
        final File file = new File(path);
        if (!file.exists()) return "";
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            StringBuilder sb = new StringBuilder(1024);
            char[] buffer = new char[2048];
            int count;
            while ((count = br.read(buffer)) != -1) {
                sb.append(buffer, 0, count);
            }
            return sb.toString();
        }
    }
}
