package seedu.tracker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.tracker.commons.core.Config;
import seedu.tracker.commons.core.LogsCenter;
import seedu.tracker.commons.core.Version;
import seedu.tracker.commons.exceptions.DataConversionException;
import seedu.tracker.commons.util.ConfigUtil;
import seedu.tracker.commons.util.StringUtil;
import seedu.tracker.logic.Logic;
import seedu.tracker.logic.LogicManager;
import seedu.tracker.model.*;
import seedu.tracker.model.util.SampleDataUtil;
import seedu.tracker.storage.*;
import seedu.tracker.ui.Ui;
import seedu.tracker.ui.UiManager;

import javax.xml.crypto.Data;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 2, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing ModuleTracker ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        UserInfoStorage userInfoStorage = new JsonUserInfoStorage(config.getUserInfoFilePath());
        UserInfo userInfo = initInfo(userInfoStorage);
        ModuleTrackerStorage moduleTrackerStorage = new JsonModuleTrackerStorage(userPrefs.getModuleTrackerFilePath());
        storage = new StorageManager(moduleTrackerStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs, userInfo);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s mod tracker and {@code userPrefs}. <br>
     * The data from the sample mod tracker will be used instead if {@code storage}'s mod tracker is not found,
     * or an empty mod tracker will be used instead if errors occur when reading {@code storage}'s mod tracker.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs, ReadOnlyUserInfo userInfo) {
        Optional<ReadOnlyModuleTracker> moduleTrackerOptional;
        ReadOnlyModuleTracker initialData;
        try {
            moduleTrackerOptional = storage.readModuleTracker();
            if (!moduleTrackerOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample ModuleTracker");
            }
            initialData = moduleTrackerOptional.orElseGet(SampleDataUtil::getSampleModuleTracker);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty ModuleTracker");
            initialData = new ModuleTracker();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty ModuleTracker");
            initialData = new ModuleTracker();
        }

        return new ModelManager(initialData, userPrefs, userInfo);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        // Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    protected UserInfo initInfo(UserInfoStorage storage) {
        Path infoFilePath = storage.getUserInfoFilePath();
        logger.info("Using info file : " + infoFilePath);

        UserInfo initializedInfo;
        try {
            Optional<UserInfo> infoOptional = storage.readUserInfo();
            initializedInfo = infoOptional.orElse(new UserInfo());
        } catch (DataConversionException e) {
            logger.warning("UserInfo file at " + infoFilePath + " is not in the correct format."
            + "Using default user info");
            initializedInfo = new UserInfo();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty ModuleTracker");
            initializedInfo = new UserInfo();
        }

        try {
            storage.saveUserInfo(initializedInfo);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedInfo;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty ModuleTracker");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Module Tracker " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Module Tracker ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
