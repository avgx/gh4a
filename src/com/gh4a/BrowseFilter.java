package com.gh4a;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.gh4a.utils.StringUtils;

public class BrowseFilter extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getDataString();
        
        String[] urlPart = url.split("/");
        
        String host = urlPart[2];
        
        String first = urlPart[3];
        if ("languages".equals(first)
                || "training".equals(first)
                || "login".equals(first)
                || "contact".equals(first)
                || "features".equals(first)) {//skip this
            
        }
        else if ("explore".equals(first)) {//https://github.com/explore
            Intent intent = new Intent().setClass(this, ExploreActivity.class);
            startActivity(intent);
        }
        else if ("blog".equals(first)) {//https://github.com/blog
            Intent intent = new Intent().setClass(this, BlogListActivity.class);
            startActivity(intent);
        }
        else {
            Gh4Application context = getApplicationContext();
            if (urlPart.length == 4) {
                String user = urlPart[3];
                
                context.openUserInfoActivity(this, user, null);
            }
            else if (urlPart.length == 5) {
                String user = urlPart[3];
                String repo = urlPart[4];
                
                context.openRepositoryInfoActivity(this, user, repo, 0);
            }
            else if (urlPart.length == 6) {
                String user = urlPart[3];
                String repo = urlPart[4];
                String action = urlPart[5];
                
                if ("issues".equals(action)) {
                    context.openIssueListActivity(this, user, repo, Constants.Issue.ISSUE_STATE_OPEN);
                }
                else if ("pulls".equals(action)) {
                    context.openPullRequestListActivity(this, user, repo, Constants.Issue.ISSUE_STATE_OPEN);
                }
                else if ("wiki".equals(action)) {
                    Intent intent = new Intent().setClass(this, WikiListActivity.class);
                    intent.putExtra(Constants.Repository.REPO_OWNER, user);
                    intent.putExtra(Constants.Repository.REPO_NAME, repo);
                    startActivity(intent);
                }
            }
            else if (urlPart.length >= 7) {
                String user = urlPart[3];
                String repo = urlPart[4];
                String action = urlPart[5];
                String id = urlPart[6];
                
                if ("issues".equals(action)) {
                    if (!StringUtils.isBlank(id)) {
                        try {
                            context.openIssueActivity(this, user, repo, Integer.parseInt(id));
                        }
                        catch (NumberFormatException e) {
                            // try issue with fragment url
                            // https://github.com/slapperwan/gh4a/issues/87#issuecomment-7680638
                            if (id.indexOf("#") != -1) {
                                id = id.substring(0, id.indexOf("#"));
                                if (TextUtils.isDigitsOnly(id)) {
                                    context.openIssueActivity(this, user, repo, Integer.parseInt(id));
                                }
                            }
                        }
                    }
                }
                else if ("pull".equals(action)) {
                    if (!StringUtils.isBlank(id)) {
                        try {
                            context.openPullRequestActivity(this, user, repo, Integer.parseInt(id));
                        }
                        catch (NumberFormatException e) {
                            // try issue with fragment url
                            // https://github.com/slapperwan/gh4a/pull/50#issuecomment-2342877
                            if (id.indexOf("#") != -1) {
                                id = id.substring(0, id.indexOf("#"));
                                if (TextUtils.isDigitsOnly(id)) {
                                    context.openPullRequestActivity(this, user, repo, Integer.parseInt(id));
                                }
                            }
                        }
                    }
                }
                else if ("commit".equals(action)) {
                    if (!StringUtils.isBlank(id)) {
                        try {
                            context.openCommitInfoActivity(this, user, repo, id, 0);
                        }
                        catch (NumberFormatException e) {
                            // Ignore non-numeric ids
                        }
                    }
                }
            }
        }
        finish();
    }
}
