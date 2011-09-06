package org.apache.karaf.ds.example.managed.provider.meta;

import aQute.bnd.annotation.metatype.Meta;

public interface MetaManagedExampleServiceProviderConfiguration {

    @Meta.AD(id = "salutation", deflt = "Hello", required = true)
    String salutation();

    @Meta.AD(id = "name", deflt = "to whom it may concern", required = true)
    String name();
}
