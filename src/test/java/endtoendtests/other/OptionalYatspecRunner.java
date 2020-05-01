
package endtoendtests.other;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.TableRunner;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class OptionalYatspecRunner extends TableRunner {

    private TableRunner runner;

    public OptionalYatspecRunner(Class<?> klass) throws InitializationError {
        super(klass);
        runner = Boolean.parseBoolean(System.getProperty("skip.yatspec.output", "false"))
                ? new TableRunner(klass)
                : new SpecRunner(klass);
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        runner.filter(filter);
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        runner.run(notifier);
    }
}
