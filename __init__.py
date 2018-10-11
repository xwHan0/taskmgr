from flask import Flask, render_template, url_for

app = Flask(__name__, static_folder='resources/public', template_folder='templates')




@app.route('/')
def index():
    return render_template('gantt.html',)


if __name__ == '__main__':
    app.run(debug=True)