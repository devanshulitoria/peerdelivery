package peerdelivers.peerdelivery;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

public class about extends AppCompatActivity {
    TextView version;
    Button fb_share,whats_app,everywhere;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        setContentView(R.layout.about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        LikeView likeView = (LikeView) findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        likeView.setObjectIdAndType(
                "https://www.facebook.com/peerdelivery/",
                LikeView.ObjectType.OPEN_GRAPH);
        version = (TextView) findViewById(R.id.tv_versionNumber);
        fb_share=(Button)findViewById(R.id.bt_fb_share);
        whats_app=(Button)findViewById(R.id.bt_whatapp_share);
        everywhere=(Button)findViewById(R.id.button_share_everywhere);
        fb_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=peerdelivers.peerdelivery"))
                        .setQuote("PeerDelivery-A community delivery platform")
                        .setShareHashtag(new ShareHashtag.Builder()
                                .setHashtag("#PeerDelivery")
                                .build()).build();
                shareDialog.show(content);
                Answers.getInstance().logContentView(new ContentViewEvent().putContentName("facebook share"));
            }
        });

        whats_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Now deliver items through your friends. Introducing #PeerDelivery.Find out more here https://goo.gl/tUpjMm");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
                Answers.getInstance().logContentView(new ContentViewEvent().putContentName("whatsapp share"));
            }
        });

        everywhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Explore a new way to deliver your items https://goo.gl/tUpjMm  #PeerDelivery");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                Answers.getInstance().logContentView(new ContentViewEvent().putContentName("other share"));
            }
        });

        PackageManager manager = this.getPackageManager();
        PackageInfo info=null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version.setText(info.versionCode+".0 BETA");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
