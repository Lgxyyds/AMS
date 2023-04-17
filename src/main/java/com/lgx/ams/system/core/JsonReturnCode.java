package com.lgx.ams.system.core;

public enum JsonReturnCode {
    NOT_LOGIN("401", false, "未登录"),  SUCCESS("200", true, "操作成功"),  FAIL("500", false, "操作失败"),  ACCESS_ERROR("403", false, "��������"),  NOT_FOUND("404", false, "����������");

    private String code;
    private boolean result;
    private String msg;

    private JsonReturnCode(String code, boolean result, String msg)
    {
        this.code = code;
        this.result = result;
        this.msg = msg;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isResult()
    {
        return this.result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public String getMsg()
    {
        return this.msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
