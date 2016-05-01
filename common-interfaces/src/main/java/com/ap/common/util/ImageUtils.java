package com.ap.common.util;

public class ImageUtils {
    private static final int K = 1;
    private static final double AMPLITUDE = Math.pow(0.5, 2);

    public static double[] adaptiveFilter(double[] data, double[] dbufferGradientHypot, int scanlineStride) {
        final int height = dbufferGradientHypot.length / scanlineStride;
        final double[] buffer = new double[(scanlineStride - 2) * (height - 2)];
        final double[] nextData = new double[data.length];
        System.arraycopy(data, 0, nextData, 0, data.length);

        for (int k = 0; k < K; k++) {
            for (int x = 1; x < scanlineStride - 1; x++) {
                for (int y = 1; y < height - 1; y++) {
                    int pointer = (x - 1) + (y - 1) * (scanlineStride - 2);
                    double n = 0;
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            int offset = (x + i) + (y + j) * scanlineStride;
                            n += w(dbufferGradientHypot, offset, AMPLITUDE);
                        }
                    }
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            int offset = (x + i) + (y + j) * scanlineStride;
                            buffer[pointer] += nextData[offset]
                                    * w(dbufferGradientHypot, offset, AMPLITUDE);
                        }
                    }
                    buffer[pointer] = buffer[pointer] / n;
                }
            }
            for (int x = 1; x < scanlineStride - 1; x++) {
                for (int y = 1; y < height - 1; y++) {
                    int pointer = (x - 1) + (y - 1) * (scanlineStride - 2);
                    nextData[x + y * scanlineStride] = buffer[pointer];
                }
            }
        }
        return buffer;
    }

    private static double w(double[] dbufferGradientHypotint, int offset, double pedgeAmplitude) {
        return Math.exp(-Math.sqrt(dbufferGradientHypotint[offset]) / (2 * pedgeAmplitude));
    }

    public static double[] gradientFilter(double[] dbufferAtan2, double[] dbuffer, int side) {
        assert dbufferAtan2.length == dbuffer.length;
        int height = dbuffer.length / side;
        double[] buffer = new double[(side - 2) * (height - 2)];
        for (int x = 1; x < side - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int cp = x + y * side;
                int dp = (x - 1) + (y - 1) * (side - 2);
                if (dbuffer[cp] > 0) {
                    switch ((int) dbufferAtan2[cp]) {
                        case 0:
                            // east and west
                            if (dbuffer[cp] > dbuffer[cp - 1] && dbuffer[cp] >= dbuffer[cp + 1]) {
                                buffer[dp] = dbuffer[cp];
                            } else {
                                buffer[dp] = 0;
                            }
                            break;
                        case 22:
                            if (dbuffer[cp] > (dbuffer[cp - 1] + dbuffer[cp + side - 1]) / 2 && dbuffer[cp] >= (dbuffer[cp + 1] + dbuffer[cp - side + 1]) / 2) {
                                buffer[dp] = dbuffer[cp];
                            } else {
                                buffer[dp] = 0;
                            }
                            break;
                        case 45:
                            // north east and south west
                            if (dbuffer[cp] > dbuffer[cp - side + 1] && dbuffer[cp] >= dbuffer[cp + side - 1]) {
                                buffer[dp] = dbuffer[cp];
                            } else {
                                buffer[dp] = 0;
                            }
                            break;
                        case 67:
                            if (dbuffer[cp] > (dbuffer[cp - side + 1] + dbuffer[cp - side]) / 2
                                    && dbuffer[cp] >= (dbuffer[cp + side - 1] + dbuffer[cp + side]) / 2) {
                                buffer[dp] = dbuffer[cp];
                            } else {
                                buffer[dp] = 0;
                            }
                            break;
                        case 90:
                            // north and south
                            if (dbuffer[cp] > dbuffer[cp - side] && dbuffer[cp] >= dbuffer[cp + side]) {
                                buffer[dp] = dbuffer[cp];
                            } else {
                                buffer[dp] = 0;
                            }
                            break;
                        case 112:
                            if (dbuffer[cp] > (dbuffer[cp - side] + dbuffer[cp - side - 1]) / 2
                                    && dbuffer[cp] >= (dbuffer[cp + side] + dbuffer[cp + side + 1]) / 2) {
                                buffer[dp] = dbuffer[cp];
                            } else {
                                buffer[dp] = 0;
                            }
                            break;
                        case 135:
                            // north west and south east
                            if (dbuffer[cp] > dbuffer[cp - side - 1] && dbuffer[cp] >= dbuffer[cp + side + 1]) {
                                buffer[dp] = dbuffer[cp];
                            } else {
                                buffer[dp] = 0;
                            }
                            break;
                        case 157:
                            if (dbuffer[cp] > (dbuffer[cp - side - 1] + dbuffer[cp - 1]) / 2 && dbuffer[cp] >= (dbuffer[cp + side + 1] + dbuffer[cp + 1]) / 2) {
                                buffer[dp] = dbuffer[cp];
                            } else {
                                buffer[dp] = 0;
                            }
                            break;
                        default:
                            assert false;
                            break;
                    }
                }
            }
        }
        return buffer;
    }

    public static double[] ImageBufferGradientHypot(double[] dx, double[] dy) {
        assert dx.length == dy.length;
        double resbuffer[] = new double[dx.length];
        for (int i = 0; i < dx.length; i++) {
            resbuffer[i] = Math.sqrt(dx[i] * dx[i] + dy[i] * dy[i]);
        }
        return resbuffer;
    }

    public static double[] ImageBufferAtan2(double[] dx, double[] dy) {
        assert dx.length == dy.length;
        double resbuffer[] = new double[dx.length];
        for (int i = 0; i < dx.length; i++) {
            double direction = Math.atan2(dy[i], dx[i]);
            double angle = direction < 0 ? direction + Math.PI : direction;
            double normd = Math.round(angle / Math.PI * 8);
            resbuffer[i] = normd == 8 ? 0 : (normd * 45 / 2);
        }
        return resbuffer;
    }

    public static byte[] dataVisualayxer(double[] dbuffer, boolean inverse) {
        // TODO add to param
        double min = 0;
        double max = 255;

        byte[] buffer = new byte[dbuffer.length * 4];
        double[] nbuffer = normalize(dbuffer, min, max);
        for (int i = 0; i < nbuffer.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (inverse) {
                    buffer[i * 4 + j] = (byte) ((int) (max - nbuffer[i]) & 0xff);
                } else {
                    buffer[i * 4 + j] = (byte) ((int) nbuffer[i] & 0xff);
                }
            }
            buffer[i * 4 + 3] = (byte) 0xff;
        }
        return buffer;
    }

    private static double[] normalize(double[] dbuffer, double min, double max) {
        assert dbuffer.length > 1;
        double[] buffer = new double[dbuffer.length];
        double bmin, bmax;
        bmin = bmax = dbuffer[0];
        for (int k = 1; k < dbuffer.length; k++) {
            if (dbuffer[k] < bmin) {
                bmin = dbuffer[k];
            } else if (dbuffer[k] > bmax) {
                bmax = dbuffer[k];
            }
        }
        double scale = (max - min) / (bmax - bmin);
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (dbuffer[i] - bmin) * scale;
        }
        return buffer;
    }

    public static double[] getSubData(double[] dbuffer, int x, int y, int scanlineStride, double width, double height) {
        double[] buffer = new double[(int) (width * height)];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i + x < scanlineStride && j + y < dbuffer.length / scanlineStride) {
                    buffer[(int) (i + j * width)] = dbuffer[i + x + (j + y) * scanlineStride];
                }
            }
        }
        return buffer;
    }
}
