package com.ibm.hcs.asset.comd;

import java.util.List;

public interface Command<T> {
    List<T> execute();
}
