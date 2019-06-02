package de.leonhard.storage;

import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class YamlEditor {
    private final File file;

    public YamlEditor(final File file) {
        this.file = file;
    }

    public List<String> read() throws IOException {
        final byte[] fileBytes = Files.readAllBytes(file.toPath());
        final String asString = new String(fileBytes);
        return new ArrayList<>(Arrays.asList(asString.split("\n")));
    }

    public List<String> getComments() throws IOException {
        return getCommentsFromLines(read());
    }

    public List<String> getHeader() throws IOException {
        return getHeaderFromLines(read());
    }

    public List<String> getFooter() throws IOException {
        return getFooterFromLines(read());
    }

    public List<String> getPureComments() throws IOException {
        return getPureCommentsFromLines(read());
    }

    public List<String> readWithoutHeaderAndFooter() throws IOException {
        return getLinesWithoutFooterAndHeaderFromLines(read());
    }

    public void write(final List<String> lines) throws IOException {
        final FileWriter writer = new FileWriter(file);
        for (final String str : lines) {
            writer.write(str + "\n");
        }
        writer.close();
    }


    public static List<String> getCommentsFromLines(final List<String> lines) {
        final List<String> result = new ArrayList<>();

        for (final String line : lines) {
            if (line.startsWith("#"))
                result.add(line);
        }
        return result;
    }

    public static List<String> getFooterFromLines(final List<String> lines) {
        final List<String> result = new ArrayList<>();
        Collections.reverse(lines);
        for (final String line : lines) {
            if (!line.startsWith("#")) {
                Collections.reverse(result);
                return result;
            }
            result.add(line);
        }
        Collections.reverse(result);
        return result;
    }

    public static List<String> getHeaderFromLines(final List<String> lines) {
        final List<String> result = new ArrayList<>();

        for (final String line : lines) {
            if (!line.startsWith("#"))
                return result;
            result.add(line);
        }
        return result;
    }

    /**
     * @return List of comments that don't belong to header or footer
     */
    public static List<String> getPureCommentsFromLines(final List<String> lines) {
        final List<String> comments = getCommentsFromLines(lines);
        final List<String> header = getHeaderFromLines(lines);
        final List<String> footer = getFooterFromLines(lines);

        comments.removeAll(header);
        comments.removeAll(footer);

        return comments;
    }

    public static List<String> getLinesWithoutFooterAndHeaderFromLines(final List<String> lines) {
        final List<String> header = getHeaderFromLines(lines);
        final List<String> footer = getFooterFromLines(lines);

        lines.removeAll(header);
        lines.removeAll(footer);

        return lines;
    }
}

