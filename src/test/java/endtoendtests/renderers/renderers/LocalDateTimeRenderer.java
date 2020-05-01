package endtoendtests.renderers.renderers;

import com.googlecode.yatspec.rendering.Renderer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeRenderer implements Renderer<LocalDateTime> {
    @Override
    public String render(LocalDateTime localDateTime) throws Exception {
        return localDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"));
    }
}
