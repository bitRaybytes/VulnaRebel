package exceptions;

public class TemplateRendererException extends RuntimeException {
    public TemplateRendererException(String message) {
        super(message);
    }

    public TemplateRendererException(String message, Exception e){
        super(message,e);
    }
}
