package cn.gq.highschoolmanger.loading;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingX{
    private Context context ;
    private ProgressDialog pd ;
    private String title ;
    private String message ;
    public LoadingX (Context context) {
        this.context = context ;
    }
    public  void  showProgressDialog () {
        pd = new ProgressDialog(context) ;
        pd.setTitle(title);
        pd.setMessage(message);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.show();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void close () {
        pd.dismiss();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
