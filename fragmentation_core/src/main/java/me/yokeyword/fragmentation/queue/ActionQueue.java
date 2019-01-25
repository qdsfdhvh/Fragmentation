package me.yokeyword.fragmentation.queue;

//import android.os.Handler;
//import android.os.Looper;
//
//import java.util.LinkedList;
//import java.util.Queue;
//
//import me.yokeyword.fragmentation.ISupportFragment;
//import me.yokeyword.fragmentation.SupportHelperKtx;
//
///**
// * The queue of perform action.
// * <p>
// * Created by YoKey on 17/12/29.
// */
//public class ActionQueue {
//    private Queue<Action> mQueue = new LinkedList<>();
//    private Handler mMainHandler;
//
//    public ActionQueue(Handler mainHandler) {
//        this.mMainHandler = mainHandler;
//    }
//
//    public void enqueue(final Action action) {
//        if (isThrottleBACK(action)) return;
//
//        if (action.getAction() == Action.Type.LOAD || mQueue.isEmpty()
//                && Thread.currentThread() == Looper.getMainLooper().getThread()) {
//            action.run();
//            return;
//        }
//
//        mMainHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                enqueueAction(action);
//            }
//        });
//    }
//
//    private void enqueueAction(Action action) {
//        mQueue.add(action);
//        if (mQueue.size() == 1) {
//            handleAction();
//        }
//    }
//
//    private void handleAction() {
//        if (mQueue.isEmpty()) return;
//
//        Action action = mQueue.peek();
//        action.run();
//
//        executeNextAction(action);
//    }
//
//    private void executeNextAction(Action action) {
////        if (action.getAction() == Action.Companion.getACTION_POP()) {
//////            ISupportFragment top = SupportHelper.getBackStackTopFragment(action.fragmentManager);
////            ISupportFragment top = SupportHelperKtx.INSTANCE.getBackStackTopFragment(action.getFragmentManager(), 0);
////            action.setDuration(top == null ? Action.Companion.getDEFAULT_POP_TIME() : top.getSupportDelegate().getExitAnimDuration());
////        }
//
//        if (action.getAction() == Action.Type.POP) {
//            ISupportFragment top = SupportHelperKtx.INSTANCE.getBackStackTopFragment(action.getFragmentManager(), 0);
//            action.setDuration(top == null ? Action.DEFAULT_POP_TIME : top.getSupportDelegate().getExitAnimDuration());
//        }
//
//        mMainHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mQueue.poll();
//                handleAction();
//            }
//        }, action.getDuration());
//    }
//
//    private boolean isThrottleBACK(Action action) {
////        if (action.getAction() == Action.Companion.getACTION_BACK()) {
////            Action head = mQueue.peek();
////            if (head != null && head.getAction() == Action.Companion.getACTION_POP()) {
////                return true;
////            }
////        }
//        if (action.getAction() == Action.Type.BACK) {
//            Action head = mQueue.peek();
//            return head != null && head.getAction() == Action.Type.POP;
//        }
//        return false;
//    }
//}
