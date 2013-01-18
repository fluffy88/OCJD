package suncertify.db;

public class Data implements DBMain {

	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

}
