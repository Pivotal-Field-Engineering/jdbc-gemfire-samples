CREATE  INDEX employee_ind ON employee(lname, fname, minit)

GO

CREATE  INDEX aunmind ON authors (au_lname, au_fname)
GO
CREATE  INDEX titleidind ON sales (title_id)
GO
CREATE  INDEX titleind ON titles (title)
GO
CREATE  INDEX auidind ON titleauthor (au_id)
GO
CREATE  INDEX titleidind ON titleauthor (title_id)
GO
CREATE  INDEX titleidind ON roysched (title_id)
GO
