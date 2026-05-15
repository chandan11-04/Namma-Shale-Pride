package com.nammashale.shalepride.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nammashale.shalepride.data.local.entity.FeedbackEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FeedbackDao_Impl implements FeedbackDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FeedbackEntity> __insertionAdapterOfFeedbackEntity;

  private final EntityDeletionOrUpdateAdapter<FeedbackEntity> __deletionAdapterOfFeedbackEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteFeedbackById;

  public FeedbackDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFeedbackEntity = new EntityInsertionAdapter<FeedbackEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `feedback` (`id`,`message`,`isAnonymous`,`senderName`,`senderEmail`,`timestamp`,`sentiment`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FeedbackEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getMessage());
        final int _tmp = entity.isAnonymous() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindString(4, entity.getSenderName());
        statement.bindString(5, entity.getSenderEmail());
        statement.bindLong(6, entity.getTimestamp());
        statement.bindString(7, entity.getSentiment());
      }
    };
    this.__deletionAdapterOfFeedbackEntity = new EntityDeletionOrUpdateAdapter<FeedbackEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `feedback` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FeedbackEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteFeedbackById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM feedback WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertFeedback(final FeedbackEntity feedback,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFeedbackEntity.insertAndReturnId(feedback);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteFeedback(final FeedbackEntity feedback,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFeedbackEntity.handle(feedback);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteFeedbackById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteFeedbackById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteFeedbackById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FeedbackEntity>> getAllFeedback() {
    final String _sql = "SELECT * FROM feedback ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"feedback"}, new Callable<List<FeedbackEntity>>() {
      @Override
      @NonNull
      public List<FeedbackEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfIsAnonymous = CursorUtil.getColumnIndexOrThrow(_cursor, "isAnonymous");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "senderEmail");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSentiment = CursorUtil.getColumnIndexOrThrow(_cursor, "sentiment");
          final List<FeedbackEntity> _result = new ArrayList<FeedbackEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FeedbackEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final boolean _tmpIsAnonymous;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAnonymous);
            _tmpIsAnonymous = _tmp != 0;
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderEmail;
            _tmpSenderEmail = _cursor.getString(_cursorIndexOfSenderEmail);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpSentiment;
            _tmpSentiment = _cursor.getString(_cursorIndexOfSentiment);
            _item = new FeedbackEntity(_tmpId,_tmpMessage,_tmpIsAnonymous,_tmpSenderName,_tmpSenderEmail,_tmpTimestamp,_tmpSentiment);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getFeedbackCount() {
    final String _sql = "SELECT COUNT(*) FROM feedback";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"feedback"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
