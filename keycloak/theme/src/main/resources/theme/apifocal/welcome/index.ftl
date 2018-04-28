<#--
Copyright (c) 2017-2018 apifocal LLC. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
