from taskmgr.app import db

class Information(db.Model):

    __tablename__ = 'information'
    id = db.Column(db.Integer, primary_key=True)
    type = db.Column(db.String(32), nullable=False)
    owner = db.Column(db.String(32))
    description = db.Column(db.Text)
    start = db.Column(db.DateTime)
    finish = db.Column(db.DateTime)
    status = db.Column(db.String(32))
    complete = db.Column(db.Integer)

    tid = db.Column(db.Integer, db.ForeignKey('Task.id'))    

    def __str__(self):
        return self.description