package com.zhangyang.charpter2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.zhangyang.charpter2.book.Book;
import com.zhangyang.charpter2.book.BookService;
import com.zhangyang.charpter2.book.BookService1;
import com.zhangyang.charpter2.book.BookService2;
import com.zhangyang.charpter2.book.IBookManager;
import com.zhangyang.charpter2.book.MyBookManager;
import com.zhangyang.charpter2.utils.Constants;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IBookManager bookManager;

    private MyBookManager myBookManager;

    private Button btnAdd;

    private Button btnShow;

    private TextView tvInfo;

    private Button btnAdd1;

    private Button btnShow1;

    private TextView tvInfo1;

    private int i = 0,j=0;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("MainActivity", "onServiceConnected");
            bookManager = IBookManager.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private ServiceConnection conn1 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("MainActivity", "onServiceConnected1");
            myBookManager = MyBookManager.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private Messenger serviceMessenger;

    private Messenger replyMessenger = new Messenger(new ReplyHandler());

    private static class ReplyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.MSG_FROM_SERVICE:
                    Log.i("reply",msg.getData().getString("reply"));
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private ServiceConnection msgConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("MainActivity", "onServiceConnected2");
            serviceMessenger = new Messenger(iBinder);
            Message msg = Message.obtain(null, Constants.MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg","hello,this is client");
            msg.setData(bundle);
            msg.replyTo = replyMessenger;
            try {
                serviceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = (Button) findViewById(R.id.btn_add_book);
        btnShow = (Button) findViewById(R.id.btn_show_book);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        btnAdd.setOnClickListener(this);
        btnShow.setOnClickListener(this);

        btnAdd1 = (Button) findViewById(R.id.btn_add_book_1);
        btnShow1 = (Button) findViewById(R.id.btn_show_book_1);
        tvInfo1 = (TextView) findViewById(R.id.tv_info_1);
        btnAdd1.setOnClickListener(this);
        btnShow1.setOnClickListener(this);
        bindService(new Intent(this, BookService.class), conn, BIND_AUTO_CREATE);
        bindService(new Intent(this, BookService1.class), conn1, BIND_AUTO_CREATE);
        bindService(new Intent(this, BookService2.class), msgConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        unbindService(conn1);
        unbindService(msgConn);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_book:
                try {
                    bookManager.addBook(new Book(i, "bookName"));
                    i++;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_show_book:
                try {
                    List<Book> bookList = bookManager.getBookList();
                    StringBuilder sb = new StringBuilder();
                    for (Book book : bookList) {
                        sb.append(book.bookName + book.bookId + "\n");
                    }
                    tvInfo.setText(sb.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_add_book_1:
                try {
                    myBookManager.addBook(new Book(i, "bookName"));
                    j++;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_show_book_1:
                try {
                    List<Book> bookList = myBookManager.getBooks();
                    StringBuilder sb = new StringBuilder();
                    for (Book book : bookList) {
                        sb.append(book.bookName + book.bookId + "\n");
                    }
                    tvInfo1.setText(sb.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
