package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Comment;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Comment")
@Component
public class CommentLocator extends Locator<Comment, Long> {

    public Comment create(Class<? extends medizin.server.domain.Comment> clazz) {
        return new Comment();
    }

    public Comment find(Class<? extends medizin.server.domain.Comment> clazz, Long id) {
        return Comment.findComment(id);
    }

    public Class<medizin.server.domain.Comment> getDomainType() {
        return Comment.class;
    }

    public Long getId(Comment comment) {
        return comment.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Comment comment) {
        return comment.getVersion();
    }
}
