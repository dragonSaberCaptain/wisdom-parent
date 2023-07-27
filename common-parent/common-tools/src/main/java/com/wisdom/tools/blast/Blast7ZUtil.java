package com.wisdom.tools.blast;

import cn.hutool.core.util.StrUtil;
import com.wisdom.tools.Thread.TaskRunnable;
import com.wisdom.tools.Thread.ThreadUtil;
import com.wisdom.tools.string.StringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 爆破 7z压缩包工具类 ZipInputStream
 *
 * @author captain
 * @version 1.0
 * @datetime 2023/1/29 13:46 星期日
 */
@Slf4j
@Data
@Accessors(chain = true)
public class Blast7ZUtil {
    static final String[] capitalLatins = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "G", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    static final String[] smallLatins = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "g", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    static final int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public static volatile boolean bl = false;

    @SuppressWarnings("resource")
    private static String unZip(String rootPath, String sourceRarPath, String destDirPath, String passWord) {
        ZipFile zipFile;
        String result = "";
        try {
            String filePath = rootPath + sourceRarPath;
            if (StringUtils.isNotBlank(passWord)) {
                zipFile = new ZipFile(filePath, passWord.toCharArray());
            } else {
                zipFile = new ZipFile(filePath);
            }
            zipFile.setCharset(Charset.forName("GBK"));
            zipFile.extractAll(rootPath + destDirPath);
        } catch (Exception e) {
            log.error("unZip error", e);
            return e.getMessage();
        }
        return result;
    }

    private static String unRar(String rootPath, String sourceRarPath, String destDirPath, String passWord) {
        String rarDir = rootPath + sourceRarPath;
        String outDir = rootPath + destDirPath + File.separator;
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        try {
            // 第一个参数是需要解压的压缩包路径，第二个参数参考JdkAPI文档的RandomAccessFile
            randomAccessFile = new RandomAccessFile(rarDir, "r");
            if (StringUtils.isNotBlank(passWord))
                inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile), passWord);
            else inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));

            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
            for (final ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                final int[] hash = new int[]{0};
                if (!item.isFolder()) {
                    ExtractOperationResult result;
                    final long[] sizeArray = new long[1];

                    File outFile = new File(outDir + item.getPath());
                    File parent = outFile.getParentFile();
                    if ((!parent.exists()) && (!parent.mkdirs())) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(passWord)) {
                        result = item.extractSlow(data -> {
                            try {
                                IOUtils.write(data, new FileOutputStream(outFile, true));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            hash[0] ^= Arrays.hashCode(data); // Consume data
                            sizeArray[0] += data.length;
                            return data.length; // Return amount of consumed
                        }, passWord);
                    } else {
                        result = item.extractSlow(data -> {
                            try {
                                IOUtils.write(data, new FileOutputStream(outFile, true));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            hash[0] ^= Arrays.hashCode(data); // Consume data
                            sizeArray[0] += data.length;
                            return data.length; // Return amount of consumed
                        });
                    }

                    if (result == ExtractOperationResult.OK) {
                        log.error("解压rar成功...." + String.format("%9X | %10s | %s", hash[0], sizeArray[0], item.getPath()));
                    } else if (StringUtils.isNotBlank(passWord)) {
                        log.error("解压rar成功：密码错误或者其他错误...." + result);
                        return "password";
                    } else {
                        return "rar error";
                    }
                }
            }

        } catch (Exception e) {
            log.error("unRar error", e);
            return e.getMessage();
        } finally {
            try {
                inArchive.close();
                randomAccessFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static void un7z(String rootPath, String sourceRarPath, String destDirPath, String passWord) throws Exception {
        File srcFile = new File(rootPath + sourceRarPath);//获取当前压缩文件
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new Exception(srcFile.getPath() + "所指文件不存在");
        }
        //开始解压
        SevenZFile zIn;
        if (StringUtils.isNotBlank(passWord)) {
            zIn = new SevenZFile(srcFile, passWord.toCharArray());
        } else {
            zIn = new SevenZFile(srcFile);
        }

        SevenZArchiveEntry entry;
        File file;
        while ((entry = zIn.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                file = new File(rootPath + destDirPath, entry.getName());
                if (!file.exists()) {
                    new File(file.getParent()).mkdirs();//创建此文件的上级目录
                }
                OutputStream out = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                int len;
                byte[] buf = new byte[2048];
                while ((len = zIn.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }
                // 关流顺序，先打开的后关闭
                bos.close();
                out.close();
            }
        }
    }

    public static String un7zCheckPwd(File srcFile, long pwd) {
        return un7zCheckPwd(srcFile, String.valueOf(pwd));
    }

    public static String un7zCheckPwd(File srcFile, String pwd) {
        SevenZFile zIn = null;
        try {
            if (StringUtils.isNotBlank(pwd)) {
                zIn = new SevenZFile(srcFile, pwd.toCharArray());
            } else {
                zIn = new SevenZFile(srcFile);
            }
            SevenZArchiveEntry entry;
            while ((entry = zIn.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    if (zIn.read() == 255) {
                        break;
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (zIn != null) {
                    zIn.close();
                }
            } catch (IOException e) {
                log.error("Blast7ZUtil--un7zCheckPwd:", e);
                e.printStackTrace();
            }
        }
        return pwd;
    }

    public static long dealNumbers(BlastDto blastDto, long minPwd, long maxPwd) {
        long curPwd = minPwd - 1;
        while (true) {
            curPwd += 1;
            if (curPwd > maxPwd) {
                System.out.println("破解失败！！！");
                return -1;
            }
            String resultPwd = un7zCheckPwd(blastDto.getSrcFile(), curPwd);
            if (StrUtil.isNotBlank(resultPwd)) {
                System.out.println("破解完成,密码:" + curPwd);
                break;
            } else {
                System.out.println("破解中...当前密码:" + curPwd);
            }
        }
        return curPwd;
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\admin\\Desktop\\mosaic\\mosaic.7z";
        //Scanner sc = new Scanner(System.in);
        //BlastDto blastDto = new BlastDto(sc);
        //long minNum = Long.parseLong(StringUtil.addRightZero("1", blastDto.getBlastLenMin())); //根据长度生成口令最小值
        //long maxNum = Long.parseLong(StringUtil.addSymbol("9", blastDto.getBlastLenMax(), false, '9'));  //根据长度生成口令最大值

        //获取当前压缩文件
        File srcFile = new File(filePath);
        long minNum = Long.parseLong(StringUtil.addRightZero("1", 3)); //根据长度生成口令最小值
        long maxNum = Long.parseLong(StringUtil.addSymbol("9", 3, false, '9'));  //根据长度生成口令最大值
        long dealNum = maxNum - minNum; //待处理数

        int corePoolSize = ThreadUtil.getExecutor().getCorePoolSize();
        long sliceSize = dealNum / corePoolSize; //计算每个线程处理数 124
        long rem = dealNum % corePoolSize; // 待处理剩余数
        for (int i = 0; i < corePoolSize; i++) {
            //System.out.println("开始位置:" + (i * sliceSize + minNum) + "   结束位置：" + (sliceSize * (i + 1) + minNum));
            ThreadUtil.execute(new TaskRunnable(srcFile, i * sliceSize, sliceSize * (i + 1)));
        }
        if (0 != rem) {
            //System.out.println("开始位置:" + (maxNum - rem) + "   结束位置：" + maxNum);
            ThreadUtil.execute(new TaskRunnable(srcFile, maxNum - rem, maxNum));
        }
        ThreadUtil.shutdownNow();
        System.out.println(ThreadUtil.getExecutor().isShutdown());
        System.out.println(ThreadUtil.getExecutor().getActiveCount());
        //while (!ThreadUtil.getExecutor().isShutdown())
        //    System.out.println("111");
    }
}
