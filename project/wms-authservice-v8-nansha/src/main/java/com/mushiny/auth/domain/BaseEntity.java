package com.mushiny.auth.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = false)
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, length = 255, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "MODIFIED_DATE")
    private ZonedDateTime modifiedDate = ZonedDateTime.now();

    @LastModifiedBy
    @Column(name = "MODIFIED_BY", length = 255)
    private String modifiedBy;

    @Column(name = "ADDITIONAL_CONTENT", length = 255)
    private String additionalContent;

    @Column(name = "ENTITY_LOCK")
    private int lock = 0;

    @Version
    @Column(name = "VERSION", nullable = false)
    private long version;

    public BaseEntity() {
    }

    public BaseEntity(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getAdditionalContent() {
        return additionalContent;
    }

    public void setAdditionalContent(String additionalContent) {
        this.additionalContent = additionalContent;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    protected void updateModifiedDate() {
        this.modifiedDate = ZonedDateTime.now();
    }

    @Transient
    public boolean isLocked() {
        return lock != 0;
    }

    @Override
    public String toString() {
        return toUniqueString();
    }

    public String toUniqueString() {
        return (getId() == null ? "" : getId().toString());
    }

    public String toDescriptiveString() {
        StringBuffer b = new StringBuffer();
        try {
            BeanInfo info = Introspector.getBeanInfo(getClass());
            java.beans.PropertyDescriptor[] d = info.getPropertyDescriptors();

            b.append(getClass().getSimpleName());
            b.append(": ");

            for (int i = 0; i < d.length; i++) {
                try {
                    if (d[i].getName().equals("class")) {
                        continue;
                    }
                    b.append("[");
                    b.append(d[i].getName());
                    b.append("=");
                    try {
                        b.append(d[i].getReadMethod().invoke(this, new Object[0]).toString());
                    } catch (Throwable t) {
                        b.append("?");
                    }
                    b.append("]");
                } catch (Throwable ex) {
                    continue;
                }
            }
            return new String(b);
        } catch (Throwable t) {
            return super.toString();
        }
    }

    public String toShortString() {
        return getClass().getSimpleName() + ": [id=" + getId() + "]";
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        // In list operations with lazy loaded entities, you may get a proxy with the correct id but a different class
        else if (obj instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) obj;

            if (getId() == null || entity.getId() == null) {
                return false;
            }
            if (entity.getId().equals(getId())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void addAdditionalContent(String s) {
        String prev = getAdditionalContent() == null ? "" : getAdditionalContent();
        setAdditionalContent(s + "\n--- " + new SimpleDateFormat().format(new Date()) + " ----\n\n" + prev);
    }

//    @PrePersist
//    public void prePersist() {
//        User createdBy = getUserOfAuthenticatedUser();
//        this.createdBy = createdBy;
//        this.createdDate = ZonedDateTime.now();
//        this.modifiedBy = createdBy;
//        this.modifiedDate = ZonedDateTime.now();
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        User modifiedBy = getUserOfAuthenticatedUser();
//        this.modifiedBy = modifiedBy;
//        this.modifiedDate = ZonedDateTime.now();
//    }
//
//    private User getUserOfAuthenticatedUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return null;
//        }
//        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
//        return user;
//    }
}
