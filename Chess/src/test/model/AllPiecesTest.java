package model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	CheckTest.class, 
	KingTest.class, 
	PawnTest.class, 
	RookTest.class, 
	KnightTest.class, 
	BishopTest.class,
	QueenTest.class})
public class AllPiecesTest {

}
