package dan200.computercraft;

import com.google.common.collect.MapMaker;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public final class PortableTickScheduler
{
    private static final Set<PortableTickSchedule> toTickAtStart = Collections.newSetFromMap( new MapMaker().weakKeys()
        .makeMap() );
    private static final Set<PortableTickSchedule> toTickAtEnd = Collections.newSetFromMap( new MapMaker().weakKeys()
        .makeMap() );

    public PortableTickScheduler()
    {
    }

    private void handleTick(Set<PortableTickSchedule> toTick) {
        Iterator<PortableTickSchedule> iterator = toTick.iterator();
        while( iterator.hasNext() )
        {
            PortableTickSchedule scheduled = iterator.next();

            if (scheduled.skipTick > 0) {
                scheduled.skipTick -= 1;
                continue;
            }

            iterator.remove();
            scheduled.task.run();
        }

    }

    public void tickAtEnd() {
        handleTick(toTickAtEnd);
    }

    public void tickAtStart() {
        handleTick(toTickAtStart);
    }

    public void scheduleOnNextStartTick(Runnable task) {
        scheduleStartTick(task, 0);
    }

    public void scheduleOnCurrentEndTick(Runnable task) {
        scheduleEndTick(task, 0);
    }

    public void scheduleOnNextEndTick(Runnable task) {
        scheduleEndTick(task, 1);
    }

    public void scheduleStartTick(Runnable task, int atTick) {
        toTickAtStart.add( new PortableTickSchedule(task, atTick) );
    }

    public void scheduleEndTick(Runnable task, int atTick) {
        toTickAtEnd.add( new PortableTickSchedule(task, atTick) );
    }

    private static class PortableTickSchedule {
        public Runnable task;
        public int skipTick;

        public PortableTickSchedule(Runnable task, int skipTick) {
           this.task = task;
           this.skipTick = skipTick;
        }
    }
}
