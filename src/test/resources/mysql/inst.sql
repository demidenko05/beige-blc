create trigger phtotgteq0 before update on PERSISTABLEHEAD for each row begin if NEW.ITSTOTAL<0 then signal sqlstate '45000' set MESSAGE_TEXT = 'itsTotal<0!'; end if; end;
