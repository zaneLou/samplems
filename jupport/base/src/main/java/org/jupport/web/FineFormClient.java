package org.jupport.web;


import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.redirect.RedirectAction;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.http.client.indirect.FormClient;
//import org.pac4j.core.client.RedirectAction;
//import org.pac4j.core.exception.RequiresHttpAction;
//import org.pac4j.http.credentials.UsernamePasswordCredentials;
//import org.pac4j.http.credentials.authenticator.UsernamePasswordAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FineFormClient extends FormClient{

	protected final Logger logger = LoggerFactory.getLogger(FineFormClient.class);
	
    public FineFormClient(final String loginUrl, final Authenticator usernamePasswordAuthenticator) {
        super(loginUrl, usernamePasswordAuthenticator);
    }
    
    @Override
    public RedirectAction getRedirectAction(final WebContext context) throws HttpAction {
    	J2EContext j2eContext = (J2EContext) context;
    	String contextPath = j2eContext.getRequest().getContextPath();
    	logger.info("++retrieveRedirectAction++" + "[j2eContext]" + j2eContext + "[contextPath]" + contextPath);
    	
    	if(contextPath.equals("/"))
    		return RedirectAction.redirect(getLoginUrl());
    	else
    		return RedirectAction.redirect( contextPath + getLoginUrl());
    }

    protected String getWebRedirectionUrl(final WebContext context, String redirectionUrl)
    {
    	J2EContext j2eContext = (J2EContext) context;
    	String contextPath = j2eContext.getRequest().getContextPath();
    	logger.info("++retrieveRedirectAction++" + "[j2eContext]" + j2eContext + "[contextPath]" + contextPath);
    	
    	if(contextPath.equals("/"))
    		return redirectionUrl;
    	else
    		return contextPath + redirectionUrl;
    }
    
    @Override
    protected UsernamePasswordCredentials retrieveCredentials(final WebContext context) throws HttpAction {
        final String username = context.getRequestParameter(getPasswordParameter());
        UsernamePasswordCredentials credentials;
        try {
            // retrieve credentials
            credentials = getCredentialsExtractor().extract(context);
            logger.debug("usernamePasswordCredentials : {}", credentials);
            if (credentials == null) {
                String redirectionUrl = CommonHelper.addParameter(getLoginUrl(), getPasswordParameter(), username);
                redirectionUrl = CommonHelper.addParameter(redirectionUrl, ERROR_PARAMETER, MISSING_FIELD_ERROR);
                logger.debug("redirectionUrl : {}", redirectionUrl);
                final String message = "Username and password cannot be blank -> return to the form with error";
                logger.debug(message);
                throw HttpAction.redirect(message, context, getWebRedirectionUrl(context, redirectionUrl));
            }
            // validate credentials
            getAuthenticator().validate(credentials, context);
        } catch (final CredentialsException e) {
            String redirectionUrl = CommonHelper.addParameter(getLoginUrl(), getPasswordParameter(), username);
            String errorMessage = computeErrorMessage(e);
            redirectionUrl = CommonHelper.addParameter(redirectionUrl, ERROR_PARAMETER, errorMessage);
            logger.debug("redirectionUrl : {}", redirectionUrl);
            final String message = "Credentials validation fails -> return to the form with error";
            logger.debug(message);
            throw HttpAction.redirect(message, context, getWebRedirectionUrl(context, redirectionUrl));
        }

        return credentials;
    }
}
