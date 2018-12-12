package plugin.gui.Tabs;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import plugin.core.comment.Comment;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;
import java.util.List;

import lombok.Data;
import plugin.gui.Items.Comments;
import plugin.gui.KotoedContext;
import plugin.gui.Utils.CommentTreeItem;
import plugin.gui.Utils.CommentTreeRenderer;

import static plugin.gui.Utils.PsiKeys.DISPLAY_GUTTER_ICONS;
import static plugin.gui.Utils.PsiKeys.PSI_KEY_COMMENT_LIST;
import static plugin.gui.Utils.Strings.*;

@Data
public class CommentsTab {

    private JPanel comentPreview;
    private JPanel comentView;
    private JPanel panel;
    private JTree fileComentTree;
    private Comments comments;

    public CommentsTab() {
    }

    public void loadComments() {

        List<Comment> commentList = Objects.requireNonNull(KotoedContext.project.getUserData(PSI_KEY_COMMENT_LIST));

        Map<Pair<String, Long>, List<Comment>> structuredComments = getStructuredComments(commentList);
        List<CommentTreeItem> commentItemsList = new ArrayList<>();

        for (Map.Entry<Pair<String, Long>, List<Comment>> i:structuredComments.entrySet()) {
            commentItemsList.add(new CommentTreeItem(i.getKey().getFirst(),i.getKey().getSecond(),i.getValue()));
        }
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        for (CommentTreeItem i:commentItemsList) {
            root.add(new DefaultMutableTreeNode(i));
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        fileComentTree.setModel(treeModel);
        fileComentTree.setRootVisible(false);

        if (!fileComentTree.isVisible()) fileComentTree.setVisible(true);

        fileComentTree.setCellRenderer(new CommentTreeRenderer());

        fileComentTree.addTreeSelectionListener(evt -> nodeSelected(evt));
        this.comentView.setLayout(new BorderLayout());

        KotoedContext.project.putUserData(DISPLAY_GUTTER_ICONS, "Display");
    }
    private void nodeSelected(TreeSelectionEvent tse){
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tse.getNewLeadSelectionPath().getLastPathComponent();
        if (node == null)
            return;
        Object nodeInfo = node.getUserObject();

        CommentTreeItem treeItem = (CommentTreeItem) nodeInfo;
        UpdateCommentArea(new Comments(treeItem).getContentPane());
    }
    private void UpdateCommentArea(JPanel p){
        this.comentView.removeAll();
        this.comentView.add(p);
        this.comentView.revalidate();
        this.comentView.repaint();
    }
    private Map<Pair<String, Long>, List<Comment>> getStructuredComments(List<Comment> commentList) {
        Map<Pair<String, Long>, List<Comment>> structuredComments = new HashMap<>();
        commentList.forEach(comment -> {
            String sourceFile = comment.getSourcefile();
            long sourceLine = comment.getSourceline();
            Pair<String, Long> pair = new Pair<>(sourceFile, sourceLine);

            if (structuredComments.containsKey(pair)) {
                structuredComments.get(pair).add(comment);
            } else {
                List<Comment> comments = new ArrayList<>();
                comments.add(comment);
                structuredComments.put(pair, comments);
            }
        });
        return structuredComments;
    }
}
