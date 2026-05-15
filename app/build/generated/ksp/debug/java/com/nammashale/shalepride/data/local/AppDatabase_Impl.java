package com.nammashale.shalepride.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.nammashale.shalepride.data.local.dao.FacilityDao;
import com.nammashale.shalepride.data.local.dao.FacilityDao_Impl;
import com.nammashale.shalepride.data.local.dao.FeedbackDao;
import com.nammashale.shalepride.data.local.dao.FeedbackDao_Impl;
import com.nammashale.shalepride.data.local.dao.MealDao;
import com.nammashale.shalepride.data.local.dao.MealDao_Impl;
import com.nammashale.shalepride.data.local.dao.PostDao;
import com.nammashale.shalepride.data.local.dao.PostDao_Impl;
import com.nammashale.shalepride.data.local.dao.StudentStarDao;
import com.nammashale.shalepride.data.local.dao.StudentStarDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile MealDao _mealDao;

  private volatile PostDao _postDao;

  private volatile FeedbackDao _feedbackDao;

  private volatile StudentStarDao _studentStarDao;

  private volatile FacilityDao _facilityDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `meals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT NOT NULL, `menu` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `uploadedBy` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_meals_date` ON `meals` (`date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `posts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `type` TEXT NOT NULL, `authorName` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `feedback` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `message` TEXT NOT NULL, `isAnonymous` INTEGER NOT NULL, `senderName` TEXT NOT NULL, `senderEmail` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `sentiment` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `student_stars` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `className` TEXT NOT NULL, `achievement` TEXT NOT NULL, `photoUrl` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `facilities` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `category` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '6fd23c057d075f2292714f249a88f4de')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `meals`");
        db.execSQL("DROP TABLE IF EXISTS `posts`");
        db.execSQL("DROP TABLE IF EXISTS `feedback`");
        db.execSQL("DROP TABLE IF EXISTS `student_stars`");
        db.execSQL("DROP TABLE IF EXISTS `facilities`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMeals = new HashMap<String, TableInfo.Column>(6);
        _columnsMeals.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeals.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeals.put("menu", new TableInfo.Column("menu", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeals.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeals.put("uploadedBy", new TableInfo.Column("uploadedBy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeals.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMeals = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMeals = new HashSet<TableInfo.Index>(1);
        _indicesMeals.add(new TableInfo.Index("index_meals_date", true, Arrays.asList("date"), Arrays.asList("ASC")));
        final TableInfo _infoMeals = new TableInfo("meals", _columnsMeals, _foreignKeysMeals, _indicesMeals);
        final TableInfo _existingMeals = TableInfo.read(db, "meals");
        if (!_infoMeals.equals(_existingMeals)) {
          return new RoomOpenHelper.ValidationResult(false, "meals(com.nammashale.shalepride.data.local.entity.MealEntity).\n"
                  + " Expected:\n" + _infoMeals + "\n"
                  + " Found:\n" + _existingMeals);
        }
        final HashMap<String, TableInfo.Column> _columnsPosts = new HashMap<String, TableInfo.Column>(7);
        _columnsPosts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosts.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosts.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosts.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosts.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosts.put("authorName", new TableInfo.Column("authorName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosts.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPosts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPosts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPosts = new TableInfo("posts", _columnsPosts, _foreignKeysPosts, _indicesPosts);
        final TableInfo _existingPosts = TableInfo.read(db, "posts");
        if (!_infoPosts.equals(_existingPosts)) {
          return new RoomOpenHelper.ValidationResult(false, "posts(com.nammashale.shalepride.data.local.entity.PostEntity).\n"
                  + " Expected:\n" + _infoPosts + "\n"
                  + " Found:\n" + _existingPosts);
        }
        final HashMap<String, TableInfo.Column> _columnsFeedback = new HashMap<String, TableInfo.Column>(7);
        _columnsFeedback.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedback.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedback.put("isAnonymous", new TableInfo.Column("isAnonymous", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedback.put("senderName", new TableInfo.Column("senderName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedback.put("senderEmail", new TableInfo.Column("senderEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedback.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedback.put("sentiment", new TableInfo.Column("sentiment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFeedback = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFeedback = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFeedback = new TableInfo("feedback", _columnsFeedback, _foreignKeysFeedback, _indicesFeedback);
        final TableInfo _existingFeedback = TableInfo.read(db, "feedback");
        if (!_infoFeedback.equals(_existingFeedback)) {
          return new RoomOpenHelper.ValidationResult(false, "feedback(com.nammashale.shalepride.data.local.entity.FeedbackEntity).\n"
                  + " Expected:\n" + _infoFeedback + "\n"
                  + " Found:\n" + _existingFeedback);
        }
        final HashMap<String, TableInfo.Column> _columnsStudentStars = new HashMap<String, TableInfo.Column>(6);
        _columnsStudentStars.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudentStars.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudentStars.put("className", new TableInfo.Column("className", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudentStars.put("achievement", new TableInfo.Column("achievement", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudentStars.put("photoUrl", new TableInfo.Column("photoUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudentStars.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStudentStars = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStudentStars = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStudentStars = new TableInfo("student_stars", _columnsStudentStars, _foreignKeysStudentStars, _indicesStudentStars);
        final TableInfo _existingStudentStars = TableInfo.read(db, "student_stars");
        if (!_infoStudentStars.equals(_existingStudentStars)) {
          return new RoomOpenHelper.ValidationResult(false, "student_stars(com.nammashale.shalepride.data.local.entity.StudentStarEntity).\n"
                  + " Expected:\n" + _infoStudentStars + "\n"
                  + " Found:\n" + _existingStudentStars);
        }
        final HashMap<String, TableInfo.Column> _columnsFacilities = new HashMap<String, TableInfo.Column>(5);
        _columnsFacilities.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacilities.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacilities.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacilities.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFacilities.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFacilities = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFacilities = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFacilities = new TableInfo("facilities", _columnsFacilities, _foreignKeysFacilities, _indicesFacilities);
        final TableInfo _existingFacilities = TableInfo.read(db, "facilities");
        if (!_infoFacilities.equals(_existingFacilities)) {
          return new RoomOpenHelper.ValidationResult(false, "facilities(com.nammashale.shalepride.data.local.entity.FacilityEntity).\n"
                  + " Expected:\n" + _infoFacilities + "\n"
                  + " Found:\n" + _existingFacilities);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "6fd23c057d075f2292714f249a88f4de", "e71e7b69160ad54705f7d3caed8b6db9");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "meals","posts","feedback","student_stars","facilities");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `meals`");
      _db.execSQL("DELETE FROM `posts`");
      _db.execSQL("DELETE FROM `feedback`");
      _db.execSQL("DELETE FROM `student_stars`");
      _db.execSQL("DELETE FROM `facilities`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MealDao.class, MealDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PostDao.class, PostDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FeedbackDao.class, FeedbackDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StudentStarDao.class, StudentStarDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FacilityDao.class, FacilityDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public MealDao mealDao() {
    if (_mealDao != null) {
      return _mealDao;
    } else {
      synchronized(this) {
        if(_mealDao == null) {
          _mealDao = new MealDao_Impl(this);
        }
        return _mealDao;
      }
    }
  }

  @Override
  public PostDao postDao() {
    if (_postDao != null) {
      return _postDao;
    } else {
      synchronized(this) {
        if(_postDao == null) {
          _postDao = new PostDao_Impl(this);
        }
        return _postDao;
      }
    }
  }

  @Override
  public FeedbackDao feedbackDao() {
    if (_feedbackDao != null) {
      return _feedbackDao;
    } else {
      synchronized(this) {
        if(_feedbackDao == null) {
          _feedbackDao = new FeedbackDao_Impl(this);
        }
        return _feedbackDao;
      }
    }
  }

  @Override
  public StudentStarDao studentStarDao() {
    if (_studentStarDao != null) {
      return _studentStarDao;
    } else {
      synchronized(this) {
        if(_studentStarDao == null) {
          _studentStarDao = new StudentStarDao_Impl(this);
        }
        return _studentStarDao;
      }
    }
  }

  @Override
  public FacilityDao facilityDao() {
    if (_facilityDao != null) {
      return _facilityDao;
    } else {
      synchronized(this) {
        if(_facilityDao == null) {
          _facilityDao = new FacilityDao_Impl(this);
        }
        return _facilityDao;
      }
    }
  }
}
