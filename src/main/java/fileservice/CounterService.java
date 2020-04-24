package fileservice;

import java.util.concurrent.atomic.AtomicLong;

public class CounterService {
  private static final AtomicLong counter = new AtomicLong(1L);

  public long execute() {
    return counter.getAndIncrement();
  }
}
