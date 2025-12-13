package org.zalmoxis.evetic.exceptions;

public class QrCodeInvalidException
        extends RuntimeException
{
    public QrCodeInvalidException(String message)
    {
        super(message);
    }

    public QrCodeInvalidException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public QrCodeInvalidException(Throwable cause)
    {
        super(cause);
    }

    public QrCodeInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public QrCodeInvalidException()
    {
    }
}
