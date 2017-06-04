+++

https://my.oschina.net/csensix/blog/184434

git config user.name "ZaneLou"
git config user.email zane@mygithub.com

ssh-keygen -t rsa -C "zane@mygithub.com"
id_rsa_mygithub
ssh-add ~/.ssh/id_rsa_mygithub 
touch ~/.ssh/config 


# 该文件用于配置私钥对应的服务器
Host github.com
HostName github.com
User git
IdentityFile ~/.ssh/id_rsa

# second user(second@mail.com)
Host mygithub
HostName github.com
User git
IdentityFile ~/.ssh/id_rsa_mygithub

more id_rsa_mygithub.pub

SSH keys


ssh-add -K ~/.ssh/github_rsa


https://help.github.com/articles/error-repository-not-found/
https://help.github.com/articles/adding-a-new-ssh-key-to-your-github-account/


ssh -T git@github.com


git@mygithub:zaneLou/samplems.git

	url = git@mygithub:zaneLou/samplems.git
	url = https://github.com/zaneLou/samplems.git
	