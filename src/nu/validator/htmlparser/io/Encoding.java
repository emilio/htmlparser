/*
 * Copyright (c) 2006 Henri Sivonen
 * Copyright (c) 2008 Mozilla Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */

package nu.validator.htmlparser.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderMalfunctionError;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class Encoding {

    public static final Encoding UTF8;

    public static final Encoding UTF16;

    public static final Encoding UTF16LE;

    public static final Encoding UTF16BE;

    public static final Encoding WINDOWS1252;

    private static Map<String, Encoding> encodingByLabel =
        new HashMap<String, Encoding>();

    /* From the table at https://encoding.spec.whatwg.org/#names-and-labels,
     * everything in the Labels column, sorted */
    private static String[] NOT_OBSCURE = { //
            "866", //
            "ansi_x3.4-1968", //
            "arabic", //
            "ascii", //
            "asmo-708", //
            "big5", //
            "big5-hkscs", //
            "chinese", //
            "cn-big5", //
            "cp1250", //
            "cp1251", //
            "cp1252", //
            "cp1253", //
            "cp1254", //
            "cp1255", //
            "cp1256", //
            "cp1257", //
            "cp1258", //
            "cp819", //
            "cp866", //
            "csbig5", //
            "cseuckr", //
            "cseucpkdfmtjapanese", //
            "csgb2312", //
            "csibm866", //
            "csiso2022jp", //
            "csiso2022kr", //
            "csiso58gb231280", //
            "csiso88596e", //
            "csiso88596i", //
            "csiso88598e", //
            "csiso88598i", //
            "csisolatin1", //
            "csisolatin2", //
            "csisolatin3", //
            "csisolatin4", //
            "csisolatin5", //
            "csisolatin6", //
            "csisolatin9", //
            "csisolatinarabic", //
            "csisolatincyrillic", //
            "csisolatingreek", //
            "csisolatinhebrew", //
            "cskoi8r", //
            "csksc56011987", //
            "csmacintosh", //
            "csshiftjis", //
            "csunicode", //
            "cyrillic", //
            "dos-874", //
            "ecma-114", //
            "ecma-118", //
            "elot_928", //
            "euc-jp", //
            "euc-kr", //
            "gb18030", //
            "gb2312", //
            "gb_2312", //
            "gb_2312-80", //
            "gbk", //
            "greek", //
            "greek8", //
            "hebrew", //
            "hz-gb-2312", //
            "ibm819", //
            "ibm866", //
            "iso-10646-ucs-2", //
            "iso-2022-cn", //
            "iso-2022-cn-ext", //
            "iso-2022-jp", //
            "iso-2022-kr", //
            "iso-8859-1", //
            "iso-8859-10", //
            "iso-8859-11", //
            "iso-8859-13", //
            "iso-8859-14", //
            "iso-8859-15", //
            "iso-8859-16", //
            "iso-8859-2", //
            "iso-8859-3", //
            "iso-8859-4", //
            "iso-8859-5", //
            "iso-8859-6", //
            "iso-8859-6-e", //
            "iso-8859-6-i", //
            "iso-8859-7", //
            "iso-8859-8", //
            "iso-8859-8-e", //
            "iso-8859-8-i", //
            "iso-8859-9", //
            "iso-ir-100", //
            "iso-ir-101", //
            "iso-ir-109", //
            "iso-ir-110", //
            "iso-ir-126", //
            "iso-ir-127", //
            "iso-ir-138", //
            "iso-ir-144", //
            "iso-ir-148", //
            "iso-ir-149", //
            "iso-ir-157", //
            "iso-ir-58", //
            "iso8859-1", //
            "iso8859-10", //
            "iso8859-11", //
            "iso8859-13", //
            "iso8859-14", //
            "iso8859-15", //
            "iso8859-2", //
            "iso8859-3", //
            "iso8859-4", //
            "iso8859-5", //
            "iso8859-6", //
            "iso8859-7", //
            "iso8859-8", //
            "iso8859-9", //
            "iso88591", //
            "iso885910", //
            "iso885911", //
            "iso885913", //
            "iso885914", //
            "iso885915", //
            "iso88592", //
            "iso88593", //
            "iso88594", //
            "iso88595", //
            "iso88596", //
            "iso88597", //
            "iso88598", //
            "iso88599", //
            "iso_8859-1", //
            "iso_8859-15", //
            "iso_8859-1:1987", //
            "iso_8859-2", //
            "iso_8859-2:1987", //
            "iso_8859-3", //
            "iso_8859-3:1988", //
            "iso_8859-4", //
            "iso_8859-4:1988", //
            "iso_8859-5", //
            "iso_8859-5:1988", //
            "iso_8859-6", //
            "iso_8859-6:1987", //
            "iso_8859-7", //
            "iso_8859-7:1987", //
            "iso_8859-8", //
            "iso_8859-8:1988", //
            "iso_8859-9", //
            "iso_8859-9:1989", //
            "koi", //
            "koi8", //
            "koi8-r", //
            "koi8-ru", //
            "koi8-u", //
            "koi8_r", //
            "korean", //
            "ks_c_5601-1987", //
            "ks_c_5601-1989", //
            "ksc5601", //
            "ksc_5601", //
            "l1", //
            "l2", //
            "l3", //
            "l4", //
            "l5", //
            "l6", //
            "l9", //
            "latin1", //
            "latin2", //
            "latin3", //
            "latin4", //
            "latin5", //
            "latin6", //
            "logical", //
            "mac", //
            "macintosh", //
            "ms932", //
            "ms_kanji", //
            "replacement", //
            "shift-jis", //
            "shift_jis", //
            "sjis", //
            "sun_eu_greek", //
            "tis-620", //
            "ucs-2", //
            "unicode", //
            "unicode-1-1-utf-8", //
            "unicode11utf8", //
            "unicode20utf8", //
            "unicodefeff", //
            "unicodefffe", //
            "us-ascii", //
            "utf-16", //
            "utf-16be", //
            "utf-16le", //
            "utf-8", //
            "utf8", //
            "visual", //
            "windows-1250", //
            "windows-1251", //
            "windows-1252", //
            "windows-1253", //
            "windows-1254", //
            "windows-1255", //
            "windows-1256", //
            "windows-1257", //
            "windows-1258", //
            "windows-31j", //
            "windows-874", //
            "windows-949", //
            "x-cp1250", //
            "x-cp1251", //
            "x-cp1252", //
            "x-cp1253", //
            "x-cp1254", //
            "x-cp1255", //
            "x-cp1256", //
            "x-cp1257", //
            "x-cp1258", //
            "x-euc-jp", //
            "x-gbk", //
            "x-mac-cyrillic", //
            "x-mac-roman", //
            "x-mac-ukrainian", //
            "x-sjis", //
            "x-unicode20utf8", //
            "x-user-defined", //
            "x-x-big5", //
    };
    private static Map<String, Encoding> encodingByCookedName = new HashMap<String, Encoding>();

    private final String canonName;

    private final Charset charset;

    static {
        Set<Encoding> encodings = new HashSet<Encoding>();

        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        for (Map.Entry<String, Charset> entry : charsets.entrySet()) {
            Charset cs = entry.getValue();
            String name = toNameKey(cs.name());
            String canonName = toAsciiLowerCase(cs.name());
            if (!isBanned(stripDashAndUnderscore(name))) {
                name = name.intern();
                boolean asciiSuperset = asciiMapsToBasicLatin(testBuf, cs);
                Encoding enc = new Encoding(canonName.intern(), cs,
                        asciiSuperset, isObscure(name),
                        isShouldNot(stripDashAndUnderscore(name)),
                        isLikelyEbcdic(name, asciiSuperset));
                encodings.add(enc);
                Set<String> aliases = cs.aliases();
                for (String alias : aliases) {
                    encodingByLabel.put(toNameKey(alias).intern(), enc);
                }
            }
        }
        // Overwrite possible overlapping aliases with the real things--just in
        // case
        for (Encoding encoding : encodings) {
            encodingByLabel.put(toNameKey(encoding.getCanonName()),
                    encoding);
        }
        UTF8 = forName("utf-8");
        UTF16 = forName("utf-16");
        UTF16BE = forName("utf-16be");
        UTF16LE = forName("utf-16le");
        WINDOWS1252 = forName("windows-1252");
    }

    public static Encoding forName(String name) {
        Encoding rv = encodingByLabel.get(toNameKey(name));
        if (rv == null) {
            throw new UnsupportedCharsetException(name);
        } else {
            return rv;
        }
    }

    public static String toNameKey(String str) {
        if (str == null) {
            return null;
        }
        int j = 0;
        char[] buf = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c += 0x20;
            }
            if (!(c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r')) {
                buf[j] = c;
                j++;
            }
        }
        return new String(buf, 0, j);
    }

    public static String stripDashAndUnderscore(String str) {
        if (str == null) {
            return null;
        }
        char[] buf = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '-' || c == '_') {
                buf[i] = c;
            }
        }
        return new String(buf);
    }

    public static String toAsciiLowerCase(String str) {
        if (str == null) {
            return null;
        }
        char[] buf = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c += 0x20;
            }
            buf[i] = c;
        }
        return new String(buf);
    }

    /**
     * @param canonName
     * @param charset
     * @param asciiSuperset
     * @param obscure
     * @param shouldNot
     * @param likelyEbcdic
     */
    private Encoding(final String canonName, final Charset charset,
            final boolean asciiSuperset, final boolean obscure,
            final boolean shouldNot, final boolean likelyEbcdic) {
        this.canonName = canonName;
        this.charset = charset;
        this.asciiSuperset = asciiSuperset;
        this.obscure = obscure;
        this.shouldNot = shouldNot;
        this.likelyEbcdic = likelyEbcdic;
    }

    /**
     * Returns the canonName.
     * 
     * @return the canonName
     */
    public String getCanonName() {
        return canonName;
    }

    /**
     * @return
     * @see java.nio.charset.Charset#canEncode()
     */
    public boolean canEncode() {
        return charset.canEncode();
    }

    /**
     * @return
     * @see java.nio.charset.Charset#newDecoder()
     */
    public CharsetDecoder newDecoder() {
        return charset.newDecoder();
    }

    /**
     * @return
     * @see java.nio.charset.Charset#newEncoder()
     */
    public CharsetEncoder newEncoder() {
        return charset.newEncoder();
    }

    protected static String msgLegacyEncoding(String name) {
        return "Legacy encoding \u201C" + name + "\u201D used. Documents must"
                + " use UTF-8.";
    }

    protected static String msgIgnoredCharset(String ignored, String name) {
        return "Internal encoding declaration specified \u201C" + ignored
                + "\u201D. Continuing as if the encoding had been \u201C"
                + name + "\u201D.";
    }
    protected static String msgNotCanonicalName(String label, String name) {
        return "The encoding \u201C" + label + "\u201D is not the canonical"
                + " name of the character encoding in use. The canonical name"
                + " is \u201C" + name + "\u201D. (Charmod C024)";
    }

    protected static String msgBadInternalCharset(String internalCharset) {
        return "Internal encoding declaration named an unsupported character"
            + " encoding \u201C" + internalCharset + "\u201D.";
    }

    protected static String msgBadEncoding(String name) {
        return "Unsupported character encoding name: \u201C" + name + "\u201D.";
    }

    public static void main(String[] args) {
        for (Map.Entry<String, Encoding> entry : encodingByLabel.entrySet()) {
            String name = entry.getKey();
            Encoding enc = entry.getValue();
            System.out.printf(
                    "%21s: canon %21s, obs %5s, reg %5s, asc %5s, ebc %5s\n",
                    name, enc.getCanonName(), enc.isObscure(),
                    enc.isRegistered(), enc.isAsciiSuperset(),
                    enc.isLikelyEbcdic());
        }
    }

}
