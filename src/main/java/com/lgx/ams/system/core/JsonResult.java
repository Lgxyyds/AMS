package com.lgx.ams.system.core;

public class JsonResult<T>
{
    private String code;
    private boolean success;
    private String msg;
    private T data;

    public String getCode()
    {
        return this.code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isSuccess()
    {
        return this.success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMsg()
    {
        return this.msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public T getData()
    {
        return (T)this.data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public String toString()
    {
        return "code=" + this.code + " message=" + this.msg + " data=" + this.data;
    }

    public static <T> JsonResult<T> success()
    {
        JsonResult<T> ret = new JsonResult();
        ret.setCode(JsonReturnCode.SUCCESS.getCode());
        ret.setSuccess(JsonReturnCode.SUCCESS.isResult());
        ret.setMsg(JsonReturnCode.SUCCESS.getMsg());
        return ret;
    }

    public static <T> JsonResult<T> successData(T data)
    {
        JsonResult<T> ret = success();
        ret.setData(data);
        return ret;
    }

    public static <T> JsonResult<T> successMessage(String msg)
    {
        JsonResult<T> ret = success();
        ret.setMsg(msg);
        return ret;
    }

    public static <T> JsonResult<T> fail()
    {
        JsonResult<T> ret = new JsonResult();
        ret.setCode(JsonReturnCode.FAIL.getCode());
        ret.setSuccess(JsonReturnCode.FAIL.isResult());
        ret.setMsg(JsonReturnCode.FAIL.getMsg());
        return ret;
    }

    public static <T> JsonResult<T> failData(T data)
    {
        JsonResult<T> ret = fail();
        ret.setData(data);
        return ret;
    }

    public static <T> JsonResult<T> failMessage(String msg)
    {
        JsonResult<T> ret = fail();
        ret.setMsg(msg);
        return ret;
    }

    public static <T> JsonResult<T> http404(T data)
    {
        JsonResult<T> ret = new JsonResult();
        ret.setCode(JsonReturnCode.NOT_FOUND.getCode());
        ret.setSuccess(JsonReturnCode.NOT_FOUND.isResult());
        ret.setMsg(JsonReturnCode.NOT_FOUND.getMsg());
        ret.setData(data);
        return ret;
    }

    public static <T> JsonResult<T> http403(T data)
    {
        JsonResult<T> ret = new JsonResult();
        ret.setCode(JsonReturnCode.ACCESS_ERROR.getCode());
        ret.setSuccess(JsonReturnCode.ACCESS_ERROR.isResult());
        ret.setMsg(JsonReturnCode.ACCESS_ERROR.getMsg());
        ret.setData(data);
        return ret;
    }
}

