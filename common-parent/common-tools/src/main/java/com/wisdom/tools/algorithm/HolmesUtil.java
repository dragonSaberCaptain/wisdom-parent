package com.wisdom.tools.algorithm;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * 福尔摩斯 加解密算法
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/11/29 9:58 星期二
 */
@Slf4j
@Data
@Accessors(chain = true)
public class HolmesUtil {
    /**
     * 密码本 字母本 a~z
     * <p>
     * . ... e s
     * .. .. i i
     * ... . s e
     */
    public static final String[] LETTER_PASSWORD_BOOK = {
            ".-", "-...", "-.-.", "-..", ".", "..-.", "--.",
            "....", "..", ".---", "-.-", ".-..", "--", "-.",
            "---", ".--.", "--.-",
            ".-.", "...", "-",
            "..-", "...-", ".--",
            "-..-", "-.--", "--.."};

    /**
     * 密码本 数字本 0~9
     */
    public static final String[] NUMBER_PASSWORD_BOOK = {"-----", ".----", "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----."};

    /**
     * 密码本 符号本
     * ？ / - . ()
     */
    public static final String[] SYMBOL_PASSWORD_BOOK = {"..--..", "-..-.", "-....-", ".-.-.-", "-.--.-"};

    public static Map<Object, Object> passwordBook;

    /**
     * 其他 约定
     * ...-.（我将重新发送最后一个单词）
     * .. ..（同样）
     * ........（错误）
     * <p>
     * 缩写和同一符号不同，缩写保留着字元中间的间隔，它们并没有被连成一个使用。
     * A - All after （问号后用于请求重复）
     * AB - All before （同样）
     * ARRL - American Radio Relay League（美国无线电中继联盟）
     * ABT - About（关于）
     * ADS - Address（地址）
     * AGN - Again（再一次）
     * ANT - Antenna （天线）
     * ABN - All between（之间的所有）
     * BUG - Semiautomatic key（半自动的关键）
     * C - Yes（是，好）
     * CLG - Calling（拨打）
     * CQ - Calling any station（连络任一站台）
     * CUL - See you later（待会见）
     * CUZ - Because（因为）
     * CW - Continuous wave（连续波）
     * CX - Conditions（状况）
     * DE - From（来自）
     * DX - Distance （sometimes refers to long distance contact）（长程通讯）
     * ES - And （和）
     * FB - Fine business （类似于“确定”）
     * FCC - Federal Communications Commission（美国联邦通信委员会）
     * FER - For （为了）
     * FREQ - Frequency（频率）
     * GA - Good afternoon or Go ahead （depending on context）（午安）
     * GE - Good evening（晚安）
     * GM - Good morning（早安）
     * GND - Ground （ground potential）（地表）
     * GD - Good（好）
     * HI - Laughter（笑；笑声）
     * HR - Here（这里）
     * HV - Have（有）
     * LID - Poor operator（可怜的运营商）
     * MILS - Milliamperes（毫安）
     * NIL - Nothing（无）
     * NR - Number（编号）
     * OB - Old boy（老男孩）
     * OC - Old chap（老兄）
     * OM - Old man （any male amateur radio operator is an OM）（任何男性业余无线电操作员是一个OM）
     * OO - Official Observer （官方观察员）
     * OP - Operator（操作员）
     * OT - Old timer（老手）
     * OTC - Old timers club （老前辈俱乐部）
     * OOTC - Old old timers club （老老前辈俱乐部）
     * PSE - Please（请）
     * PWR - Power（功率）
     * QCWA - Quarter Century Wireless Association （四分之一世纪无线协会）
     * R I - acknowledge or decimal point （承认或小数点）（根据上下文确定）
     * RCVR - Receiver（接收器）
     * RPT - Repeat or report（重复或报告）（根据上下文确定）
     * RST - Signal report format （Readability-Signal Strength-Tone）（收讯指标）
     * RTTY - Radio teletype （无线电电传）
     * RX - Receive（接收）
     * SAE - Self addressed envelope （自我处理的信封）
     * SASE - Self addressed, stamped envelope （自我解决，盖章信封）
     * SED - Said（说）
     * SEZ - Says （说）
     * SIG - Signal（讯号）
     * SIGS - Signals （信号）
     * SKED - Schedule（行程）
     * SN - Soon（很快=不久将来）
     * SOS -（紧急呼救=国际通用）
     * SRI - Sorry（抱歉）
     * STN - Station（电台）
     * TEMP - Temperature（气温）
     * TMW - Tomorrow（明日）
     * TNX - Thanks（感谢）
     * TU - Thank you（感谢你）
     * TX - Transmit（发射器）
     * U - You （你）
     * UR - Your or you're （您或您已经）（根据上下文确定）
     * URS - Yours （你的）
     * VY - Very （非常）
     * WDS - Words （词语）
     * WKD - Worked （工作）
     * WL - Will （将）
     * WUD - Would （会）
     * WX - Weather（天气）
     * XMTR - Transmitter（发射机）
     * XYL - Wife（妻子）
     * YL - Young lady （used of any female）（年轻女子）
     * 73 - Best regards（最好的祝福）
     * 88 - Love and kisses（爱与吻之告别）（注意应该使用在“异性”之间）
     * 99 - go away（被要求离开，非友善）
     */
    public static void init() {
        if (null == passwordBook) {
            passwordBook = new HashMap<>();
        }
        //26个小写字母
        for (int i = 0; i < LETTER_PASSWORD_BOOK.length; i++) {
            passwordBook.put((char) (97 + i), LETTER_PASSWORD_BOOK[i]);
        }
        //0~9个数字
        for (int i = 0; i < NUMBER_PASSWORD_BOOK.length; i++) {
            passwordBook.put((char) (48 + i), NUMBER_PASSWORD_BOOK[i]);
        }
        passwordBook.put("?", "..--..");
        passwordBook.put("/", "-..-.");
        passwordBook.put("-", "-....-");
        passwordBook.put(".", ".-.-.-");
        passwordBook.put("()", "-.--.-");
        passwordBook.put("AR", ".-.-."); //停止，消息结束
        passwordBook.put("AS", ".-..."); //等待
        passwordBook.put("K", "-.-"); //邀请发射信号,一般跟随AR，表示“该你了
        passwordBook.put("SK", "...-.-"); //终止，联络结束
        passwordBook.put("BT", "-...-"); //分隔符
    }

    public static String encryption(String value) {
        char[] chars = value.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char aChar : chars) {
            sb.append(LETTER_PASSWORD_BOOK[aChar - 'a']);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(encryption("cab"));
    }
}
