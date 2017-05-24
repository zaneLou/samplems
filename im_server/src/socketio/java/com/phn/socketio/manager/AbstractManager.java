package com.phn.socketio.manager;

import java.util.Date;

import org.springframework.util.StringUtils;

import com.google.protobuf.ByteString;
import com.phn.base.component.PhnAuthenticator;
import com.phn.base.component.PhnConstants;
import com.phn.proto.PhnNetBuf.PhnDataBuf;
import com.phn.proto.PhnNetBuf.PhnResponseBuf;

public abstract class AbstractManager {

    public abstract PhnAuthenticator getAuthenticator();

    public PhnDataBuf getPhnDataBufBuiler(String token, String dataId, int dataType, boolean hashed, String lang, String device, ByteString dataObj) {
        PhnDataBuf.Builder builer = PhnDataBuf.newBuilder().setDataType(dataType);
        if (StringUtils.hasLength(dataId)) {
            builer.setDataId(dataId);
        }
        if (hashed) {
            builer.setHash(getAuthenticator().hash(dataId + ":" + token));
        }
        if (StringUtils.hasLength(device)) {
            builer.setDevice(device);
        }
        if (StringUtils.hasLength(lang)) {
            builer.setLang(lang);
        }
        if (dataObj != null) {
            builer.setDataObj(dataObj);
        }
        return builer.build();
    }

    public PhnResponseBuf getPhnResponseBuf(int code, String content, ByteString dataObj) {
    	PhnResponseBuf.Builder builer = PhnResponseBuf.newBuilder().setCode(code);
    	if (StringUtils.hasLength(content)) {
    		builer.setContent(content);
    	}
    	if (dataObj!=null) {
    		builer.setContentBytes(dataObj);
    	}
    	return builer.build();
    }

    public PhnDataBuf getPhnDataBuf(int dataType, int code, ByteString dataObj) {
        PhnResponseBuf responseBuf = getPhnResponseBuf(code, PhnConstants.getResponseMessage(code), dataObj);
        Long dataId = new Date().getTime();
        return getPhnDataBufBuiler(null, dataId.toString(), dataType, false, null, null, responseBuf.toByteString());
    }
}
