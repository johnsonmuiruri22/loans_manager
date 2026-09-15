package com.loan_manager_app.loans_manager;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModularityTests {
    ApplicationModules modules =
            ApplicationModules.of(LoansManagerApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }

}
