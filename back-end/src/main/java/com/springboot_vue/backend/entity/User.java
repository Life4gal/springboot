package com.springboot_vue.backend.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.springboot_vue.backend.common.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "sys_user")
public class User extends BaseEntity<Long> {

	private static final long serialVersionUID = -4454737765850239378L;

	@NotBlank
	@Column(name = "account", unique = true, length = 10)
	private String account;

	@NotBlank
	@Column(name = "nickname", length = 10)
	private String nickname;

	/**
	 * 使用md5(username + original password + salt)加密存储
	 */
	@NotBlank
	@Column(name = "password", length = 64)
	private String password;

	/**
	 * 加密密码时使用的盐
	 */
	private String salt;

	/**
	 * 头像
	 */
	private String avatar;

	@Column(name = "email", unique = true, length = 20)
	private String email;  // 邮箱

	@Column(name = "mobile_phone_number", length = 20)
	private String mobilePhoneNumber;

	/**
	 * 系统用户的状态
	 */
	@Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.normal;


	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	/**
	 * 最后一次登录时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "last_login")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	/**
	 * 是否是管理员
	 */
	private Boolean admin = false;

	/**
	 * 是否已注销
	 */
	private Boolean deleted = Boolean.FALSE;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "User [account=" + account + ", nickname=" + nickname + ", password=" + password + ", salt=" + salt
				+ ", avatar=" + avatar + ", email=" + email + ", mobilePhoneNumber=" + mobilePhoneNumber + ", status=" + status
				+ ", createDate=" + createDate + ", lastLogin=" + lastLogin + ", admin=" + admin + ", deleted=" + deleted + "]";
	}

}
