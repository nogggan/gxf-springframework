package com.gxf.gxfframework.resolver;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xiao Feng
 * 功能描述：判断此次请求是否是web请求（用于区分PC端、APP端）
 * @创建时间: 2018/12/29
 */
public class MediaTypeService {

    private ContentNegotiationManager contentNegotiationManager;

    public MediaTypeService(ContentNegotiationManager contentNegotiationManager){
        this.contentNegotiationManager = contentNegotiationManager;
    }

    public boolean isWebRequest(NativeWebRequest request){
        List<MediaType> mediaTypes = new ArrayList<>();
        boolean isWeb = false;
        try {
            if(null!=contentNegotiationManager)
                mediaTypes = contentNegotiationManager.resolveMediaTypes(request);
        } catch (HttpMediaTypeNotAcceptableException e1) {
            e1.printStackTrace();
        }
        isWeb = mediaTypes.stream().anyMatch(mediaType -> {
            String mediaTypeString = mediaType.getType()+"/"+mediaType.getSubtype();
            return "text/html".equals(mediaTypeString);
        });
        return isWeb;
    }

}
