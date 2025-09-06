package ru.skypro.homework.exceptions;

/**
 * Исключение, выбрасываемое при попытке зарегистрировать пользователя с уже существующим username.
 */
public class UsernameExistsException extends RuntimeException {

    public UsernameExistsException(String message) {
        super(message);
    }

    public UsernameExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}