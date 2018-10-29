
from app import db


class Task(db.Model):

    __tablename__ = 'Task'
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(255), nullable=False)
    typ = db.Column(db.String(32), nullable=False)
    style = db.Column(db.String(64))

    pid = db.Column(db.Integer, db.ForeignKey('Task.id'))

    sub = db.relationship('Task', lazy='dynamic')    
    info = db.relationship('Information', backref='task', lazy='dynamic')
    
    def __init__(self, title='', pid=0, typ='', style=''):
        self.title = title
        self.pid = pid
        self.typ = typ
        self.style = style
        
    def complete(self, idx = []):
        self.plan = self.sub.filter(typ='plan').all()
        self.owner = self.plan[-1].owner
        self.finish = self.plan[-1].finish
        self.status = self.plan[-1].status
        
        self.identity = 'Task' + '_'.join(map(str, idx))
        
        return self
            
    def __str__(self):
        return '[Task:{0}] id={1} typ={2} style={3}'.format(self.title, self.id, self.typ, self.style)

