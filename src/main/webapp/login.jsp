<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, sans-serif;
        }

        body {
            background-color: #fff;
            color: #24292f;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 20px;
        }

        .login-container {
            width: 100%;
            max-width: 308px;
        }

        .logo {
            text-align: center;
            margin-bottom: 16px;
            font-size: 24px;
            font-weight: 300;
            letter-spacing: -0.5px;
        }

        .login-box {
            background-color: #ffffff;
            border: 1px solid #d8dee4;
            border-radius: 6px;
            padding: 16px;
            margin-bottom: 16px;
        }

        .login-title {
            font-size: 24px;
            font-weight: 300;
            letter-spacing: -0.5px;
            text-align: center;
            margin-bottom: 16px;
        }

        .form-group {
            margin-bottom: 16px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 400;
            font-size: 14px;
        }

        .form-control {
            margin-top: 4px;
            padding: 5px 12px;
            font-size: 14px;
            line-height: 20px;
            color: #24292f;
            vertical-align: middle;
            background-color: #ffffff;
            background-repeat: no-repeat;
            background-position: right 8px center;
            border: 1px solid #d0d7de;
            border-radius: 6px;
            width: 100%;
            min-height: 32px;
        }

        .form-control:focus {
            border-color: #0969da;
            outline: none;
            box-shadow: 0 0 0 3px rgba(9, 105, 218, 0.3);
        }

        .btn {
            position: relative;
            display: inline-block;
            padding: 5px 16px;
            font-size: 14px;
            font-weight: 500;
            line-height: 20px;
            white-space: nowrap;
            vertical-align: middle;
            cursor: pointer;
            user-select: none;
            border: 1px solid;
            border-radius: 6px;
            appearance: none;
            width: 100%;
            text-align: center;
        }

        .btn-primary {
            color: #ffffff;
            background-color: #2da44e;
            border-color: rgba(27, 31, 36, 0.15);
        }

        .btn-primary:hover {
            background-color: #2c974b;
        }

        .error {
            padding: 8px;
            margin-bottom: 16px;
            color: #cf222e;
            background-color: #ffebe9;
            border: 1px solid #ffcecb;
            border-radius: 6px;
            font-size: 14px;
        }

        .login-footer {
            padding: 16px;
            text-align: center;
            border: 1px solid #d8dee4;
            border-radius: 6px;
            font-size: 14px;
        }

        .login-footer a {
            color: #0969da;
            text-decoration: none;
        }

        .login-footer a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="logo">
        <img
                src="img/logo.jpg"
                style="width: 80px; height: 80px; border: 1px solid #d8dee4; border-radius: 50%; padding: 5px"
                alt="Logo"
        >
    </div>

    <div class="login-box">
        <h1 class="login-title">Se connecter</h1>

        <% if(request.getParameter("error") != null) { %>
        <div class="error">
            Identifiants incorrects
        </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Nom d'utilisateur</label>
                <input type="text" id="username" name="username" class="form-control" required autofocus>
            </div>

            <div class="form-group">
                <label for="password">Mot de passe</label>
                <input type="password" id="password" name="password" class="form-control" required>
            </div>

            <button type="submit" class="btn btn-primary">Se connecter</button>
        </form>
    </div>

    <div class="login-footer">
        Pas encore inscrit ? <a href="register.jsp">S'inscrire</a>
    </div>
</div>
</body>
</html>