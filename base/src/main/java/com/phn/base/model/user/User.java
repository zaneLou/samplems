/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phn.base.model.user;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.core.style.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phn.base.model.Account;

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@Entity
@Table(name = "phn_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends Account {
	
    @Column(name = "avatat_site", length=60 )
	protected String	avatatSite = "";
    
    @Column(name = "avatar_name", length=60 )
	protected String	avatarName = "";
    
    @Column(name = "avatar_size", length=60 )
	protected String	avatarSize = "";
    
    @Column(name = "telephone")
    //@NotEmpty
    //@Digits(fraction = 0, integer = 10)
    private String telephone;
    
    @Column(name = "wx_open_id")
    private String wxOpenId;

    @Column(name = "create_date")
    private Date createDate;
    
    @Column(name = "realname")
    protected String realname;
    
    @Column(name = "nickname")
    protected String nickname;
    
    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "had_info")
    private boolean hadInfo;

	@Column(name = "update_time")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date 		updateTime;
	
	@Column(name = "register_time")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date 		registerTime;
	
	@Column(name = "country", length=50)
	protected String 	country ="";
	
    @Column(name = "tokens", length=256)
    private String tokens;

    @Column(name = "source_type")
	protected int	 	sourceType;
    
    @Column(name = "source_id")
	protected String	sourceId;
    
	/*
	 * Properties
	 */
	
	public String getNickname() {
		return nickname;
	}

	public String getTokens() {
		return tokens;
	}

	public void setTokens(String tokens) {
		this.tokens = tokens;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	public boolean isHadInfo() {
		return hadInfo;
	}

	public void setHadInfo(boolean hadInfo) {
		this.hadInfo = hadInfo;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date crateDate) {
		this.createDate = crateDate;
	}

	public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getAvatatSite() {
		return avatatSite;
	}

	public void setAvatatSite(String avatatSite) {
		this.avatatSite = avatatSite;
	}

	public String getAvatarName() {
		return avatarName;
	}

	public void setAvatarName(String avatarName) {
		this.avatarName = avatarName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAvatarSize() {
		return avatarSize;
	}

	public void setAvatarSize(String avatarSize) {
		this.avatarSize = avatarSize;
	}

	@Override
    public String toString() {
        return new ToStringCreator(this)
            .append("id", this.getId())
            .append("new", this.isNew())
            .append("userName", this.getUsername())
            .append("mayRoles", this.getRolesData())
            .append("address", this.address)
            .append("city", this.city)
            .append("telephone", this.telephone)
            .toString();
    }
}
