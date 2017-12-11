package com.aplus.pillreminder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aplus.pillreminder.R;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    public static String TAG = ReportFragment.class.getSimpleName();
    @BindView(R.id.container_report) FrameLayout container_report;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, rootView);
        TreeNode root = TreeNode.root();
        TreeNode parent = new TreeNode("MyParentNode");
        TreeNode child1 = new TreeNode("Ch1");
        TreeNode child2 = new TreeNode("Ch2");
        parent.addChildren(child1, child2);
        root.addChild(parent);

        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
        container_report.addView(tView.getView());
        return rootView;
    }

}
