package com.ssukssugi.ssukssugilji.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1783535555L;

    public static final QUser user = new QUser("user");

    public final com.ssukssugi.ssukssugilji.common.QBaseEntity _super = new com.ssukssugi.ssukssugilji.common.QBaseEntity(this);

    public final BooleanPath agreeToReceiveMarketing = createBoolean("agreeToReceiveMarketing");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final StringPath emailAddress = createString("emailAddress");

    public final EnumPath<com.ssukssugi.ssukssugilji.user.dto.LoginType> loginType = createEnum("loginType", com.ssukssugi.ssukssugilji.user.dto.LoginType.class);

    public final StringPath socialId = createString("socialId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

