package ru.shirykalov.anatoly.classiconline;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import ru.shirykalov.anatoly.classiconline.remote.HttpRemoteManager;

@Layout(R.layout.tinder_comment_view)
public class TinderCommentCard {


    @View(R.id.commentTextView)
    private TextView commentTxt;

    private Comment comment;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public TinderCommentCard(Context context, Comment comment, SwipePlaceHolderView swipeView) {
        mContext = context;
        this.comment = comment;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved(){
        commentTxt.setText(comment.getText());
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        String payload = "{\"declined\":["+comment.getId()+"],\"approved\":[]}";
        Log.d("POST", payload);
        new PostDataTask(mContext).execute(payload);
    }

    @SwipeIn
    private void onSwipedIn(){
        Log.d("EVENT", "onSwipedIn");
        String payload = "{\"approved\":["+comment.getId()+"],\"declined\":[]}";
        Log.d("POST", payload);
        new PostDataTask(mContext).execute(payload);
    }

}
