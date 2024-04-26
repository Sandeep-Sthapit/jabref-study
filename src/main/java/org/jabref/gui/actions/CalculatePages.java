package org.jabref.gui.actions;

import java.util.List;
import java.util.function.Supplier;


import org.jabref.gui.DialogService;
import org.jabref.gui.LibraryTab;
import org.jabref.gui.StateManager;
import org.jabref.gui.util.TaskExecutor;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.BibEntryTypesManager;
import org.jabref.preferences.PreferencesService;

import static org.jabref.gui.actions.ActionHelper.needsDatabase;

public class CalculatePages extends SimpleCommand {

    private final Supplier<LibraryTab> tabSupplier;
    private final DialogService dialogService;
    private final StateManager stateManager;

    private final PreferencesService prefs;
    private final BibEntryTypesManager entryTypesManager;
    private final TaskExecutor taskExecutor;

    public CalculatePages(Supplier<LibraryTab> tabSupplier,
                          DialogService dialogService,
                          StateManager stateManager,
                          PreferencesService prefs,
                          BibEntryTypesManager entryTypesManager,
                          TaskExecutor taskExecutor) {
        this.tabSupplier = tabSupplier;
        this.dialogService = dialogService;
        this.stateManager = stateManager;
        this.prefs = prefs;
        this.entryTypesManager = entryTypesManager;
        this.taskExecutor = taskExecutor;

        this.executable.bind(needsDatabase(stateManager));
    }

    @Override
    public void execute() {
        BibDatabaseContext database = stateManager.getActiveDatabase().orElseThrow(() -> new NullPointerException("Database null"));
        List<BibEntry> entries = database.getEntries();
        for(BibEntry entry: entries){
            entry.setPageTotal();
        }
        dialogService.notify("Page Total Calculated.");
    }

}

