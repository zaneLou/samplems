package com.phn.embryo;

import com.corundumstudio.socketio.scheduler.SchedulerKey;

public class EmbryoAckSchedulerKey extends SchedulerKey {

    private final long index;

    public EmbryoAckSchedulerKey(Type type, String sessionId, long index) {
        super(type, sessionId);
        this.index = index;
    }

    public long getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) (index ^ (index >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmbryoAckSchedulerKey other = (EmbryoAckSchedulerKey) obj;
        if (index != other.index)
            return false;
        return true;
    }

}

