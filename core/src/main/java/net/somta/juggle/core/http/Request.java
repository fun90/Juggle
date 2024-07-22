/*
Copyright (C) 2022-2024 husong

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, visit <https://www.gnu.org/licenses/gpl-3.0.html>.
*/
package net.somta.juggle.core.http;

import feign.template.Template;
import feign.template.UriTemplate;
import net.somta.juggle.core.enums.ParameterPositionEnum;
import net.somta.juggle.core.enums.RequestTypeEnum;
import net.somta.juggle.core.model.InputParameter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 请求实体
 * @author husong
 * @since 1.0.0
 */
public class Request {

    /**
     * 请求url,带域名的完整地址
     */
    private String requestUrl;
    /**
     * 请求方法类型 GET POST PUT DELETE
     */
    private RequestTypeEnum requestType;

    /**
     * 请求头
     */
    private Map<String,Object> requestHeaders;

    /**
     * 请求参数
     */
    private Map<String,Object> requestParams;

    /**
     * 请求超时时间 单位：毫秒
     */
    private Integer timeout;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 重试间隔时间 单位：毫秒
     */
    private Integer retryInterval = 1000;

    public Request(RequestTypeEnum requestType) {
        this.requestType = requestType;
    }

    public void initUrlAndRequestParams(String apiUrl, List<InputParameter> inputParameterSchema, Map<String, Object> inputParamData) {
        if (requestUrl == null) {
            throw new IllegalArgumentException("reqMethod,reqHeaders,reqBody should not be null");
        }
        this.requestUrl = requestUrl;
    }

    private String buildRequestUrl(String apiUrl, List<InputParameter> inputParameterSchema, Map<String, Object> inputParamData){
        if(CollectionUtils.isEmpty(inputParameterSchema)){
            return apiUrl;
        }
        List<InputParameter> pathInputParameterSchema = inputParameterSchema.stream()
                .filter(p -> ParameterPositionEnum.PATH.getCode().equals(p.getPosition()))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(pathInputParameterSchema)){
            return apiUrl;
        }
        for (InputParameter inputParamSchema : pathInputParameterSchema) {

        }

        //feign能实现这个功能
        //UriTemplate template = new Template("/users/{userId}");

        //todo 将参数 http://baidu.com/info/{id}/{type}  传入map的值 替换url 获取新的连接地址


        return apiUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public RequestTypeEnum getRequestType() {
        return requestType;
    }

    public Map<String, Object> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, Object> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, Object> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetryCount() {
        return retryCount;
    }
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }

}