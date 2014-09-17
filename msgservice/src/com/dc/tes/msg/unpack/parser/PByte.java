package com.dc.tes.msg.unpack.parser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.dc.tes.exception.CommonErr;
import com.dc.tes.exception.TESException;
import com.dc.tes.msg.util.Value;
import com.dc.tes.util.HexStringUtils;
import com.dc.tes.util.PackUtils;

/**
 * 字符串的解析器 从字节流中按照字符串的格式解析数据
 * 
 * @author lijic
 * 
 */
@ParserTag('B')
public class PByte extends Parser {
	@Override
	protected Value parse(byte[] bytes, int start, int length, Map<String, String> params) {
		if (length == -1)
			return null;

//		String encoding = MapUtils.getString(params, "encoding");
//		char fillingChar = MapUtils.getString(params, "fillingChar", " ").charAt(0);
//		boolean left_align = MapUtils.getString(params, "align", "left").equalsIgnoreCase("left");
//
//		return new Value(PackUtils.ReadString(bytes, start, length, encoding, fillingChar, left_align));
		byte[] _bytes = new byte[length];
		System.arraycopy(bytes, start, _bytes, 0, length);
		return new Value(HexStringUtils.ToHexString(_bytes));
	}

	@Override
	protected int convert(byte[] bytes, int start, int length, Map<String, String> params) {
		//String encoding = MapUtils.getString(params, "encoding");
//
//		try {
//			return new String(bytes, start, bytes.length - start).substring(0, length).getBytes(encoding).length;
//		} catch (UnsupportedEncodingException ex) {
//			throw new TESException(CommonErr.UnsupportedEncoding, encoding);
//		}
		return length;
	}
}
