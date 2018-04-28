<!--
  ~ JBoss, Home of Professional Open Source.
  ~ Copyright (c) 2011, Red Hat, Inc., and individual contributors
  ~ as indicated by the @author tags. See the copyright.txt file in the
  ~ distribution for a full listing of individual contributors.
  ~
  ~ This is free software; you can redistribute it and/or modify it
  ~ under the terms of the GNU Lesser General Public License as
  ~ published by the Free Software Foundation; either version 2.1 of
  ~ the License, or (at your option) any later version.
  ~
  ~ This software is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ Lesser General Public License for more details.
  ~
  ~ You should have received a copy of the GNU Lesser General Public
  ~ License along with this software; if not, write to the Free
  ~ Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  ~ 02110-1301 USA, or see the FSF site: http://www.fsf.org.
  -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
    <title>Welcome to apifocal identities</title>

    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />

    <link rel="shortcut icon" href="welcome-content/favicon.ico" type="image/x-icon">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700" rel="stylesheet">
    <link  href="welcome-content/keycloak.css" rel="stylesheet">
</head>

<body>
<div class="wrapper">
    <div class="content">
        <div class="logo">
          <img src="welcome-content/logo.png" alt="apifocal" border="0" />
        </div>
        <h1>Welcome to <span>Apifocal ID</span></h1>
        <h2>Managing your identities at scale</h2>

        <#if successMessage?has_content>
            <p>${successMessage}</p>
        <#elseif errorMessage?has_content>
            <p class="error">${errorMessage}</p>
        <#elseif bootstrap>
            <#if localUser>
                <p>Please create an initial admin user to get started.</p>
            <#else>
                <p>
                    You need local access to create the initial admin user. Open <a href="http://localhost:8080/auth">http://localhost:8080/auth</a>
                    or use the add-user-keycloak script.
                </p>
            </#if>
        </#if>

        <#if bootstrap && localUser>
            <form method="post">
                <div class="form-group">
                    <input class="form-control" id="username" name="username" placeholder="Username" required />
                </div>
                <div class="form-group">
                    <input class="form-control" id="password" name="password" type="password" placeholder="Password" required />
                </div>
                <div class="form-group">
                    <input class="form-control" id="passwordConfirmation" name="passwordConfirmation" type="password" placeholder="Password confirmation" required />
                </div>

                <input id="stateChecker" name="stateChecker" type="hidden" value="${stateChecker}" />
                <button id="create-button" type="submit" class="btn btn-primary">Create</button>
            </form>
        <#else>
        <p>
          <a href="admin/" class="btn btn-primary">Administration Console</a>
        </p>
        </#if>
        <p>
          <a href="https://apifocal.com/" target="_blank">Visit the apifocal website</a>
        </p>
    </div>
</div>
</body>
</html>
