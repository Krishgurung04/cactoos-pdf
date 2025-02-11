/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2023 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.pdf;

import org.cactoos.Text;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * MultiText.
 *
 * @since 0.0.1
 */
@SuppressWarnings("PMD.UseStringBufferForStringAppends")
public final class MultiText implements Object {
    /**
     * New line character.
     */
    private static final char NEW_LINE = '\n';

    /**
     * Space separator.
     */
    private static final String SPACE_SEPARATOR = " ";

    /**
     * Split regular expression.
     */
    private static final String SPLIT_REGEX = "\\s+";

    /**
     * Number.
     */
    private final int number;

    /**
     * Generation.
     */
    private final int generation;

    /**
     * Font size.
     */
    private final int size;

    /**
     * Position X.
     */
    private final int posx;

    /**
     * Position Y.
     */
    private final int posy;

    /**
     * Max line length.
     */
    private final int max;

    /**
     * Space between lines.
     */
    private final int space;

    /**
     * Text content.
     */
    private final Text content;

    /**
     * Ctor.
     *
     * @param count Count object
     * @param size Font size
     * @param posx Position X
     * @param posy Position Y
     * @param max Max line length
     * @param space Space between lines
     * @param content Text content
     * @checkstyle ParameterNumberCheck (10 lines)
     */
    public MultiText(
        final Count count,
        final int size,
        final int posx,
        final int posy,
        final int max,
        final int space,
        final Text content
    ) {
        this(count.increment(), 0, size, posx, posy, max, space, content);
    }

    /**
     * Ctor.
     *
     * @param number Object number
     * @param generation Object generation
     * @param size Font size
     * @param posx Position X
     * @param posy Position Y
     * @param max Max line length
     * @param space Space between lines
     * @param content Text content
     * @checkstyle ParameterNumberCheck (10 lines)
     */
    public MultiText(
        final int number,
        final int generation,
        final int size,
        final int posx,
        final int posy,
        final int max,
        final int space,
        final Text content
    ) {
        this.number = number;
        this.generation = generation;
        this.size = size;
        this.posx = posx;
        this.posy = posy;
        this.max = max;
        this.space = space;
        this.content = content;
    }

    @Override
    public String reference() {
        return new UncheckedText(
            new FormattedText(
                "%d %d R",
                this.number,
                this.generation
            )
        ).asString();
    }

    @Override
    public byte[] with(final Object... objects) throws Exception {
        final StringBuilder out = new StringBuilder();
        final String[] lines = breakLines(this.content.asString(), this.max);
        for (int idx = 0; idx < lines.length - 1; ++idx) {
            out.append(
                new FormattedText(
                    "(%s) Tj T*\n",
                    lines[idx]
                ).asString()
            );
        }
        out.append(
            new FormattedText(
                "(%s) Tj",
                lines[lines.length - 1]
            ).asString()
        );
        final String stream = new FormattedText(
            "BT /F1 %d Tf %d %d Td %d TL\n%s\nET",
            this.size,
            this.posx,
            this.posy,
            this.space,
            out.toString()
        ).asString();
        return new FormattedText(
            "%d %d obj\n<< /Length %d >>\nstream\n%s\nendstream\nendobj\n",
            this.number,
            this.generation,
            stream.length(),
            stream
        ).asString().getBytes();
    }

    /**
     * Break a big text into lines.
     *
     * @param input String to be split
     * @param max Max line length
     * @return The lines
     */
    private static String[] breakLines(final String input, final int max) {
        final String[] words = input.split(MultiText.SPLIT_REGEX);
        final StringBuilder output = new StringBuilder();
        int length = 0;
        for (int idx = 0; idx < words.length; ++idx) {
            String word = words[idx];
            if (
                length + (MultiText.SPACE_SEPARATOR + word).length() > max
            ) {
                if (idx > 0) {
                    output.append(MultiText.NEW_LINE);
                }
                length = 0;
            }
            if (idx < words.length - 1
                && length + (word + MultiText.SPACE_SEPARATOR).length()
                + words[idx + 1].length() <= max) {
                word = word + MultiText.SPACE_SEPARATOR;
            }
            output.append(word);
            length = length + word.length();
        }
        return output.toString().split("\n");
    }
}
