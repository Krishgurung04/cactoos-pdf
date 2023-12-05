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

import java.io.File;
import java.nio.file.Files;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Test case for {@link Document}.
 *
 * @since 0.0.1
 */
final class DocumentTest {
    @Test
    void buildDocument() {
        final String title = "Hello World";
        new Assertion<>(
            "Must represent a PDF document",
            new TextOf(
                new Document(
                    new Metadata(1, 0, title),
                    new Catalog(
                        2,
                        0,
                        new Pages(
                            3,
                            0,
                            new Page(
                                4,
                                0,
                                new TextStream(
                                    5,
                                    0,
                                    18,
                                    0,
                                    0,
                                    title
                                )
                            )
                        )
                    )
                )
            ),
            new IsText(
                new FormattedText(
                    new Joined(
                        "\n",
                        "%%PDF-1.3\n%%���������",
                        "1 0 obj\n<< /Title (Hello World) >>\nendobj",
                        "2 0 obj\n<< /Type /Catalog /Pages 3 0 R >>\nendobj",
                        "3 0 obj\n<< /Type /Pages /Kids [4 0 R] /Count 1 /MediaBox [0 0 595 842] >>\nendobj",
                        "4 0 obj\n<< /Type /Page /Parent 3 0 R /Resources << /Font << /F1 << /Type /Font /Subtype /Type1 /BaseFont /Times-Roman >> >> >> /Contents 5 0 R >>\nendobj",
                        "5 0 obj\n<< /Length 55 >>\nstream\nBT /F1 18 Tf 0 0 Td (Hello World) Tj ET\nendstream\nendobj",
                        "trailer << /Root 2 0 R /Size 5 >>",
                        "%%%%%%%%EOF"
                    ),
                    title
                )
            )
        ).affirm();
    }

    @Disabled
    @Test
    void build() throws Exception {
        final String title = "Hello World";
        final File file = new File("HelloWorld.pdf");
        Files.write(
            file.toPath(),
            new Document(
                new Metadata(1, 0, title),
                new Catalog(
                    2,
                    0,
                    new Pages(
                        3,
                        0,
                        new Page(
                            4,
                            0,
                            new TextStream(
                                5,
                                0,
                                18,
                                0,
                                0,
                                title
                            )
                        )
                    )
                )
            ).asBytes()
        );
    }
}